package org.example.person;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;	
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("v1")
public class PersonController {

    private static final String template = "Hello, %s!";
    
    
    @LoadBalanced
    @Bean
    RestTemplate restTemplate(){
      return new RestTemplate();
    }

    @Autowired
    RestTemplate restTemplate;
    

    @ApiOperation(value = "ping", nickname = "ping")
    @RequestMapping(method = RequestMethod.GET, path="/ping", produces ="text/plain")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "User's name", required = false, dataType = "string", paramType = "query", defaultValue="Niklas")
      })
    @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @ResponseBody
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return String.format(template, name);
    }
    
    @ApiOperation(value = "details", nickname = "details")
    @RequestMapping(method = RequestMethod.GET, path="/details", produces ="text/plain")
    @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    @ResponseBody
    public String getDetails(@RequestParam(value="name", defaultValue="World") String name) {
    	Set<String> dept = this.restTemplate.getForObject("http://dept-service/dept/v1/dept", Set.class);
    	
    	dept.forEach( str -> System.out.println(str));
    	
    	return "Success";
    }
    
}
