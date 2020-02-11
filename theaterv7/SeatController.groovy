package theaterv7

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class SeatController {

    SeatService seatService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond seatService.list(params), model:[seatCount: seatService.count()]
    }

    def show(Long id) {
        respond seatService.get(id)
    }

    def create() {
        respond new Seat(params)
    }

    def save(Seat seat) {
        if (seat == null) {
            notFound()
            return
        }

        try {
            seatService.save(seat)
        } catch (ValidationException e) {
            respond seat.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'seat.label', default: 'Seat'), seat.id])
                redirect seat
            }
            '*' { respond seat, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond seatService.get(id)
    }

    def update(Seat seat) {
        if (seat == null) {
            notFound()
            return
        }

        try {
            seatService.save(seat)
        } catch (ValidationException e) {
            respond seat.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'seat.label', default: 'Seat'), seat.id])
                redirect seat
            }
            '*'{ respond seat, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        seatService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'seat.label', default: 'Seat'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'seat.label', default: 'Seat'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
