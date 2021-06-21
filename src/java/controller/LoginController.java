/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import restauranteapp.BLL.LoginJpaController;
import restauranteapp.DAL.Entidade;

/**
 *
 * @author kevin
 */

public class LoginController extends AbstractController {
    
    public LoginController() {
    }
    
    protected ModelAndView handleRequestInternal(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
            response.setHeader("Cache-control", "no-cache");
            response.setHeader("Cache-control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expire", 0);
            
            HttpSession session = request.getSession();
            
            String user = request.getParameter("login");
            String passwd = request.getParameter("password");
            
            LoginJpaController loginCheck = new LoginJpaController();
            
            if(loginCheck.validateLoginWeb(user, passwd)){
                Entidade temp = loginCheck.findClienteUsername(user);
                return new ModelAndView("index", "entidade", temp);
                
            } else return new ModelAndView("login", "", null);

        
        
    }
    
}
