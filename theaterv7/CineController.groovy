package theaterv7

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.web.servlet.ModelAndView

@Secured('IS_AUTHENTICATED_ANONYMOUSLY')
class CineController {

    static layout = 'etheater'


    def index() {

        def listado = Theater.findAll();
        return new ModelAndView("listado_cine",[listado:listado]);

    }


    def ver_cine() {

        def ver_cine  = Theater.get(params.id)
        return new ModelAndView( "cine" , [datosCine:ver_cine])


    }
}


