package com.spring.project.Contoller;

import com.spring.project.Models.UserData;
import com.spring.project.Service.Token.InvalidTokenException;
import com.spring.project.Service.Userr.UserAlreadyExists;
import com.spring.project.Service.Userr.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
public class RegistrationContoller {
    private static final String REDIRECT_LOGIN= "redirect:/login";
    //private static final String REDIRECT = "redirect :";
    @Autowired
    private UserService userService;
    @Resource
    MessageSource messageSource;

    @GetMapping("/register")
    public String register(final Model model){
        model.addAttribute("userData", new UserData());
        return "account/register";
    }

    @PostMapping("/register")
    public String userRegistration(final @Valid UserData userData, final BindingResult bindingResult, final Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("registrationForm", userData);
            return "account/register";
        }
        try {
            userService.register(userData);
        }catch (UserAlreadyExists e){
            bindingResult.rejectValue("email", "userData.email","An account already exists for this email.");
            model.addAttribute("registrationForm", userData);
            return "account/register";
        }
        return "account/home";
    }


    @GetMapping("/verify")
    public String verifyCustomer(@RequestParam(required = false) String token, final Model model, RedirectAttributes redirAttr){
      if(StringUtils.isEmpty(token)){
          redirAttr.addFlashAttribute("tokenError", messageSource.getMessage("user.registration.verification.missing.token", null, LocaleContextHolder.getLocale()));
            return "/account/register";
        }
       try {
         userService.verifyUser(token);
      } catch (InvalidTokenException e) {
          redirAttr.addFlashAttribute("tokenError", messageSource.getMessage("user.registration.verification.invalid.token", null,LocaleContextHolder.getLocale()));
          return "/account/register";
       }

        redirAttr.addFlashAttribute("verifiedAccountMsg", messageSource.getMessage("user.registration.verification.success", null,LocaleContextHolder.getLocale()));
        return "account/register";
    }
    @GetMapping("veri")
    public String verii()
    {return"account/home";}
}
