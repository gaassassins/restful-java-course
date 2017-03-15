package service;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@RestController
public class Controller {
    @RequestMapping("/repos")
    public String greeting(@RequestParam(value = "user", required = false, defaultValue = "gaassassins") String user)
            throws IOException {
        URL url = new URL("https://api.github.com/users/" + user + "/repos"/* + "?format=json"*/);
        return IOUtils.toString(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));
        //получение списка всех реп юзера
    }

    @RequestMapping("/branches")
    public String greeting(@RequestParam(value = "user", required = false, defaultValue = "gaassassins") String user,
                           @RequestParam(value = "repo", required = false, defaultValue = "project-411") String repo)
            throws IOException {
        URL url = new URL("https://api.github.com/repos/" + user + "/" + repo + "/branches" /*+ "/?format=json"*/);
        return IOUtils.toString(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));
        //получение списка бранчей в определенной репе юзера
    }

    //сваггер
    //написать метод count которым считать кол-во бранчей график рисовать по колву бранчей в репах
}
