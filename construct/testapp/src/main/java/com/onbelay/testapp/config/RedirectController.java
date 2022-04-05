package com.onbelay.testapp.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping(value = {
            "/{regex:\\w+}",
            "/{x:^(?!api$).*$}/**/{regex:\\w+}",
            "/{regex:?!(\\.js|\\.css|\\.woff|\\.svg)$}"
    })
    public String forward404() {
        return "forward:/";
    }

}
