package service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Api(description = "Endpoint for countries API Git Hackers")
@RestController
public class RController {


    @ApiOperation(value = "Returns repos list",
            notes = "Returns a list of repositories.",
            responseContainer = "array",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of repositories list", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error") })
    @RequestMapping(value = "/repos", method = RequestMethod.GET)
    public String greeting(@RequestParam(value = "user", required = false, defaultValue = "gaassassins") String user)
            throws IOException {
        URL url = new URL("https://api.github.com/users/" + user + "/repos"/* + "?format=json"*/);
        return IOUtils.toString(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));
        //получение списка всех реп юзера
    }

    @ApiOperation(value = "Returns branches list",
            notes = "Returns a list of branches.",
            responseContainer = "array",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of branches list", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error") })
    @RequestMapping(value = "/branches", method = RequestMethod.GET)
    public String greeting(@RequestParam(value = "user", required = false, defaultValue = "gaassassins") String user,
                           @RequestParam(value = "repo", required = false, defaultValue = "project-411") String repo)
            throws IOException {
        URL url = new URL("https://api.github.com/repos/" + user + "/" + repo + "/branches" /*+ "/?format=json"*/);
        return IOUtils.toString(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));
        //получение списка бранчей в определенной репе юзера
    }

    //написать метод count которым считать кол-во бранчей график рисовать по колву бранчей в репах
}
