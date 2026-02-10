package com.bajaj.bfhl.bajab.controller;

import com.bajaj.bfhl.bajab.service.AiService;
import com.bajaj.bfhl.bajab.service.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/")
public class BfhlController {
    private static final String EMAIL = "keshav0713.be23@chitkara.edu.in";
    @Autowired
    private MathService mathService;
    @Autowired
    private AiService aiService;
    @GetMapping("/health-check")
    public ResponseEntity<Map<String,Object>> health() {
        Map<String,Object> res = new HashMap<>();
        res.put("is_success" , true);
        res.put("official_emial", EMAIL);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/bfhl")
    public ResponseEntity<?> bfhl(@RequestBody Map<String , Object> body) {
        if(body.size() != 1) {
            return badRequest("Request must contain exactly one value");
        }
        String key = body.keySet().iterator().next();
        Object value = body.get(key);

        try {
            Object data;

            switch (key) {
                case "fibonacci":
                    if (!(value instanceof Number)) {
                        return badRequest("fibonacci must be a number");
                    }
                    int n = ((Number) value).intValue();
                    data = mathService.fibonacci(n);
                    break;
                case "prime":
                    data = mathService.primes((List<Integer>) value);
                    break;
                case "lcm":
                    data = mathService.lcm((List<Integer>) value);
                    break;
                case "hcf":
                    data = mathService.hcf((List<Integer>) value);
                    break;
                case "AI":
                    data = aiService.ask(value.toString());
                    break;
                default:
                    return unprocessable("Invalid Response");
            }
            return ResponseEntity.ok(success(data));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Interal error"));
        }
    }

    private Map<String , Object> success(Object data) {
        Map<String , Object> res = new HashMap<>();
        res.put("is_success" , true);
        res.put("official_email", EMAIL);
        res.put("data",data);
        return res;
    }
    private Map<String , Object> error(String msg) {
        Map<String , Object> res = new HashMap<>();
        res.put("is_success" , false);
        res.put("official_email" , EMAIL);
        res.put("error" , msg);
        return res;
    }

    private ResponseEntity<?> badRequest(String msg) {
        return ResponseEntity.status(400).body(error(msg));
    }
    private ResponseEntity<?> unprocessable(String msg) {
        return ResponseEntity.status(422).body(error(msg));
    }



}
