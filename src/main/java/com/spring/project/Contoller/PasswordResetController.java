package com.spring.project.Contoller;

import com.spring.project.Models.ResetPasswordData;
import com.spring.project.Service.Token.InvalidTokenException;
import com.spring.project.Service.Userr.CustumorAccountService;
import com.spring.project.Service.Userr.UnknownIdentifierException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/password")
public class PasswordResetController {
    private static final String REDIRECT_LOGIN = "redirect:/login";
    private static final String MSG = "resetPasswordMsg";
@Autowired
    MessageSource messageSource;
@Autowired
    CustumorAccountService custumorAccountService;


   @PostMapping("/request")
   public String resetPassword(final ResetPasswordData forgotPasswordForm, RedirectAttributes redirAttr){
       try {
           custumorAccountService.forgottenPassword(forgotPasswordForm.getPassword());
       } catch (UnknownIdentifierException e) {

       }
       redirAttr.addFlashAttribute(MSG,messageSource.getMessage("user.forgot.msg",null, LocaleContextHolder.getLocale()));
       return REDIRECT_LOGIN;
   }
   @GetMapping("/change")
    public String changePassword(@RequestParam(required = false) String token,final RedirectAttributes redirAttr, final Model model)
   {if(StringUtils.isEmpty(token)){
       redirAttr.addFlashAttribute("tokenError",
               //user.registration.verification.missing.token"
               messageSource.getMessage("token is invalid",
                       null,
                       LocaleContextHolder.getLocale()));
       return REDIRECT_LOGIN;
   }
   ResetPasswordData data=new ResetPasswordData();
       data.setToken(token);
        setResetPasswordForm(model,data);
       return "account/changepassword";
   }
   @PostMapping("/change")
    public String changePassword(final ResetPasswordData data, final Model model){
       try {
   custumorAccountService.updatePassword(data.getPassword(), data.getToken());
       }
       catch (InvalidTokenException | UnknownIdentifierException exception) {
           model.addAttribute("tokenError",
                   messageSource.getMessage("user.registration.verification.invalid.token", null, LocaleContextHolder.getLocale())
           );
           return "account/changepassword";
       }
       model.addAttribute("passwordUpdateMsg",
               messageSource.getMessage("user.password.update.msg", null, LocaleContextHolder.getLocale())
       );
       setResetPasswordForm(model,new ResetPasswordData());
       return "/account/changepassword";
   }
    private void setResetPasswordForm(final Model model, ResetPasswordData data){
        model.addAttribute("forgotPassword",data);
    }
}
