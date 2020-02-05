package theaterv7

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class TheaterController {

    TheaterService theaterService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond theaterService.list(params), model:[theaterCount: theaterService.count()]
    }

    def show(Long id) {
        respond theaterService.get(id)
    }

    def create() {
        respond new Theater(params)
    }

    def save(Theater theater) {
        if (theater == null) {
            notFound()
            return
        }

        try {
            theaterService.save(theater)
        } catch (ValidationException e) {
            respond theater.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'theater.label', default: 'Theater'), theater.id])
                redirect theater
            }
            '*' { respond theater, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond theaterService.get(id)
    }

    def update(Theater theater) {
        if (theater == null) {
            notFound()
            return
        }

        try {
            theaterService.save(theater)
        } catch (ValidationException e) {
            respond theater.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'theater.label', default: 'Theater'), theater.id])
                redirect theater
            }
            '*'{ respond theater, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        theaterService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'theater.label', default: 'Theater'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'theater.label', default: 'Theater'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
