package com.bajaj.bfhl.bajab.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MathService {
    public List<Integer> fibonacci(int n) {
        if (n<=0) throw new IllegalArgumentException();
        List<Integer> res = new ArrayList<>();
        int prev2 = 0,prev1 = 1;
        for(int i = 0 ; i < n ; i++) {
            res.add(prev2);
            int curr = prev1+prev2;
            prev2 = prev1;
            prev1 = curr;
        }
        return res;
    }
    public List<Integer> primes(List<Integer> nums) {
        return nums.stream().filter(this::isPrime).collect(Collectors.toList());
    }
    private boolean isPrime(int n) {
        if (n < 2) return false;
        for(int i = 2 ; i * i <= n ; i++) {
            if(n%i == 0) {
                return false;
            }
        }
        return true;
    }
    public int hcf(List<Integer> nums) {
        int res = nums.get(0);
        for(int i : nums) {
            res = gcd(res,i);
        }
        return res;
    }
    public int lcm(List<Integer> nums) {
        int res = nums.get(0);
        for(int i : nums) {
            res = res * i / gcd(res,i);
        }
        return res;
    }
    private int gcd(int res , int ind){
        while(ind != 0) {
            int temp = ind;
            ind = res % ind;
            res = temp;
        }
        return res;
    }

}
