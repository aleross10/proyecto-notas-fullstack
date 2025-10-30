package com.notesapp.notes_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Redirige todas las rutas que NO son de API y NO son la raíz,
 * de vuelta al index.html de Angular.
 */
@Controller
public class SpaRoutingController {

    /**
     * Redirige a la raíz (que sirve index.html).
     * El regex [^\\.]* significa "cualquier ruta que no contenga un punto",
     * para evitar que intercepte solicitudes de archivos estáticos (como main.js).
     *
     * ¡EL CAMBIO CLAVE ESTÁ AQUÍ!
     * Hemos quitado el "/" de la lista de RequestMapping.
     * Ahora solo intercepta rutas como "/login", "/notas", etc.
     */
    @RequestMapping(value = "/{path:[^\\.]*}")
    public String forwardToSpa() {
        return "forward:/"; // Reenvía a la raíz (index.html)
    }
}

