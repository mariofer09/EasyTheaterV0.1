package theaterv7

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class RoomController {

    RoomService roomService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond roomService.list(params), model:[roomCount: roomService.count()]
    }

    def show(Long id) {
        respond roomService.get(id)
    }

    def create() {
        respond new Room(params)
    }

    def save(Room room) {
        if (room == null) {
            notFound()
            return
        }

        try {
            roomService.save(room)
        } catch (ValidationException e) {
            respond room.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'room.label', default: 'Room'), room.id])
                redirect room
            }
            '*' { respond room, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond roomService.get(id)
    }

    def update(Room room) {
        if (room == null) {
            notFound()
            return
        }

        try {
            roomService.save(room)
        } catch (ValidationException e) {
            respond room.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'room.label', default: 'Room'), room.id])
                redirect room
            }
            '*'{ respond room, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        roomService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'room.label', default: 'Room'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'room.label', default: 'Room'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
