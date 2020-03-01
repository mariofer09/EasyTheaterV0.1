package theaterv7

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

@Secured('IS_AUTHENTICATED_ANONYMOUSLY')
class ActorController {

    ActorService actorService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond actorService.list(params), model:[actorCount: actorService.count()]
    }

    def show(Long id) {
        respond actorService.get(id)
    }

    def create() {
        respond new Actor(params)
    }

    def save(Actor actor) {
        if (actor == null) {
            notFound()
            return
        }

        try {
            actorService.save(actor)
        } catch (ValidationException e) {
            respond actor.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'actor.label', default: 'Actor'), actor.id])
                redirect actor
            }
            '*' { respond actor, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond actorService.get(id)
    }

    def update(Actor actor) {
        if (actor == null) {
            notFound()
            return
        }

        try {
            actorService.save(actor)
        } catch (ValidationException e) {
            respond actor.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'actor.label', default: 'Actor'), actor.id])
                redirect actor
            }
            '*'{ respond actor, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        actorService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'actor.label', default: 'Actor'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'actor.label', default: 'Actor'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
