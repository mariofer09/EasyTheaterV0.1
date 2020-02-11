package theaterv7

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class MovieController {

    MovieService movieService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond movieService.list(params), model:[movieCount: movieService.count()]
    }

    def show(Long id) {
        respond movieService.get(id)
    }

    def create() {
        respond new Movie(params)
    }

    def save(Movie movie) {
        if (movie == null) {
            notFound()
            return
        }

        try {
            movieService.save(movie)
        } catch (ValidationException e) {
            respond movie.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'movie.label', default: 'Movie'), movie.id])
                redirect movie
            }
            '*' { respond movie, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond movieService.get(id)
    }

    def update(Movie movie) {
        if (movie == null) {
            notFound()
            return
        }

        try {
            movieService.save(movie)
        } catch (ValidationException e) {
            respond movie.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'movie.label', default: 'Movie'), movie.id])
                redirect movie
            }
            '*'{ respond movie, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        movieService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'movie.label', default: 'Movie'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'movie.label', default: 'Movie'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
