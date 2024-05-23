package elkar_ekin.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Locale;

@Controller
@RequestMapping("/locale")
public class LocaleController {

    @GetMapping("/change")
    public String changeLocale(@RequestParam("lang") String lang, HttpServletRequest request, HttpServletResponse response) {
        Locale locale = new Locale(lang);
        request.getSession().setAttribute("lang", locale);
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/currentLocale")
    public String currentLocale(HttpServletRequest request) {
        java.util.Locale locale = RequestContextUtils.getLocale(request);
        return "Current Locale: " + locale.toString();
    }
}
