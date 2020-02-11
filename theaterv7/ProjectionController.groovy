package theaterv7

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class ProjectionController {

    ProjectionService projectionService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond projectionService.list(params), model:[projectionCount: projectionService.count()]
    }

    def show(Long id) {
        respond projectionService.get(id)
    }

    def create() {
        respond new Projection(params)
    }

    def save(Projection projection) {
        if (projection == null) {
            notFound()
            return
        }

        try {
            projectionService.save(projection)
        } catch (ValidationException e) {
            respond projection.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'projection.label', default: 'Projection'), projection.id])
                redirect projection
            }
            '*' { respond projection, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond projectionService.get(id)
    }

    def update(Projection projection) {
        if (projection == null) {
            notFound()
            return
        }

        try {
            projectionService.save(projection)
        } catch (ValidationException e) {
            respond projection.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'projection.label', default: 'Projection'), projection.id])
                redirect projection
            }
            '*'{ respond projection, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        projectionService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'projection.label', default: 'Projection'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'projection.label', default: 'Projection'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
