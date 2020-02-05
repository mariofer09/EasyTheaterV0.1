package theaterv7

import org.springframework.web.servlet.ModelAndView

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


