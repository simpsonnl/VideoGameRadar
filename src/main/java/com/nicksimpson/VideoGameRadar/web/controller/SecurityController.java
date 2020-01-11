package com.nicksimpson.VideoGameRadar.web.controller;

import com.nicksimpson.VideoGameRadar.model.Game;
import com.nicksimpson.VideoGameRadar.model.Sort;
import com.nicksimpson.VideoGameRadar.model.User;
import com.nicksimpson.VideoGameRadar.service.UserDetailsServiceImpl;
import com.nicksimpson.VideoGameRadar.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class SecurityController {

  @Autowired
  UserDetailsServiceImpl userDetailsService;


@RequestMapping("/login")
  public String login(Model model){
  model.addAttribute("header","Login");
  return "user/login";
}

  @RequestMapping(value = "/logout", method = RequestMethod.GET)
  public String logoutDo(HttpServletRequest request, HttpServletResponse response){
    HttpSession session = request.getSession(false);
    SecurityContextHolder.clearContext();
    session= request.getSession(false);
    if(session != null) {
      session.invalidate();
    }
    for(Cookie cookie : request.getCookies()) {
      cookie.setMaxAge(0);
    }

    return "redirect:/";
  }

  @RequestMapping(value = "/postLogin", method = RequestMethod.POST)
  public String postLogin(Model model, HttpSession session) {

    // read principal out of security context and set it to session
    UsernamePasswordAuthenticationToken
        authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    validatePrinciple(authentication.getPrincipal());
    String loggedInUser = ((UserDetails) authentication.getPrincipal()).getUsername();
    session.setAttribute("currentUser", loggedInUser);

    return "redirect:/";
  }

  //checks all the new account details with requirements
  @RequestMapping(value = "/createAccount", method = RequestMethod.POST)
  public String saveAccount(User user, RedirectAttributes redirectAttributes, HttpServletRequest request){
    //if user already exists
    if(userDetailsService.exists(user)) {
      redirectAttributes.addFlashAttribute("flash",new FlashMessage("Username taken.", FlashMessage.Status.FAILURE));
      return "redirect:/create/account";
//      return String.format("redirect:%s",request.getHeader("referer"));
    }
    if(user.getUsername().length() < 3 || user.getUsername().length() > 20) {
      redirectAttributes.addFlashAttribute("flash",new FlashMessage("Username must be within 3 and 20 characters.", FlashMessage.Status.FAILURE));
      return "redirect:/create/account";
    }
    if(user.getPassword().length() < 3 || user.getPassword().length() > 20) {
      redirectAttributes.addFlashAttribute("flash",new FlashMessage("Password must be within 3 and 20 characters.", FlashMessage.Status.FAILURE));
      return "redirect:/create/account";
    }


    userDetailsService.save(user);

    redirectAttributes.addFlashAttribute("flash",new FlashMessage("Account created successfully.", FlashMessage.Status.SUCCESS));

    return "redirect:/login";
  }

  //page to create an account
  @RequestMapping(value = "/create/account")
  public String createAccount(Model model){
    model.addAttribute("header","Create Account");
    model.addAttribute("user",new User());
    return "user/createAccount";
  }

  private void validatePrinciple(Object principal) {
    if (!(principal instanceof UserDetails)) {
      throw new  IllegalArgumentException("Principal can not be null!");
    }
  }

}
