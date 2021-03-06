package airbnb.controller;

import airbnb.authentication.IAuthenticationFacade;
import airbnb.model.ApartmentEntity;
import airbnb.model.OwnerEntity;
import airbnb.model.RenterEntity;
import airbnb.model.MessagesEntity;
import airbnb.model.UsersEntity;
import airbnb.model.Pager;
import airbnb.repository.MessagesRepository;
import airbnb.repository.OwnerRepository;
import airbnb.repository.RenterRepository;
import airbnb.service.ApartmentService;
import airbnb.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Σταυρίνα on 21/9/2017.
 */
@Controller
public class MessagesController {

    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 5;
    private static final int[] PAGE_SIZES = { 5, 10, 20 };

    @Autowired
    private UsersService userService;

    /*@Autowired
    private MessagesService messagesService;*/

    @Qualifier("messagesRepository")
    @Autowired
    private MessagesRepository messagesRepository;

    @Qualifier("renterRepository")
    @Autowired
    private RenterRepository renterRepository;

    @Autowired
    @Qualifier("ownerRepository")
    private OwnerRepository ownerRepository;

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @RequestMapping(value="/question/{apartmentID}", method = RequestMethod.POST)
    public ModelAndView question( @PathVariable String apartmentID, @RequestParam("question") String Question){
        ModelAndView modelAndView = new ModelAndView();
        Authentication authentication = authenticationFacade.getAuthentication();
        modelAndView.addObject("uname", authentication.getName());
        UsersEntity userS = userService.findByUsername(authentication.getName());
        modelAndView.addObject("type", String.valueOf(userS.getType()));

        Integer apartmentID_ = Integer.parseInt(apartmentID);
        ApartmentEntity ap = apartmentService.findById(apartmentID_);

        RenterEntity renter = userService.findRenterByUsername(userS.getUsername());
        OwnerEntity owner=ap.getOwner();
        saveMessage(Question,renter,apartmentID_,ap.getName(),owner);


        System.out.println("prosthesa to message");


        modelAndView.setViewName("redirect:/apartment/"+apartmentID);
        return modelAndView;
    }

    private void saveMessage(String Question,RenterEntity renter,Integer apartmentID_,String apartment_name,OwnerEntity owner)
    {
        MessagesEntity messagesEntity=new MessagesEntity();
        messagesEntity.setQuestion(Question);
        messagesEntity.setOwnerto(owner);
        messagesEntity.setRenterfrom(renter);
        messagesEntity.setApart_id(apartmentID_);
        messagesEntity.setApart_name(apartment_name);
        messagesRepository.save(messagesEntity);

        Set<MessagesEntity> messsagesEntitySet1= owner.getMessages();
        messsagesEntitySet1.add(messagesEntity);
        ownerRepository.save(owner);
        Set<MessagesEntity> messsagesEntitySet2= renter.getMessages();
        messsagesEntitySet2.add(messagesEntity);
        renterRepository.save(renter);
    }

    @RequestMapping(value={"/messagesOwner"}, method = RequestMethod.GET/*, produces= "application/javascript"*/)
    public ModelAndView messagesOwner(@RequestParam("pageSize") Optional<Integer> pageSize,
                                   @RequestParam("page") Optional<Integer> page){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/messages");
        Authentication authentication = authenticationFacade.getAuthentication();
        if (!authentication.getName().equals("anonymousUser")) {
            modelAndView.addObject("uname", authentication.getName());
            UsersEntity userS = userService.findByUsername(authentication.getName());
            modelAndView.addObject("type", String.valueOf(userS.getType()));
        }
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Pager pager=null;
        OwnerEntity owner=userService.findOwnerByUsername(authentication.getName());
        Page<MessagesEntity> msg=messagesRepository.findByAllByOwner(owner,new PageRequest(evalPage, evalPageSize));

        pager= new Pager(msg.getTotalPages(), msg.getNumber(), BUTTONS_TO_SHOW);
        if(msg.getTotalElements()!=0){
            modelAndView.addObject("pager", pager);
            modelAndView.addObject("items", msg);
            modelAndView.addObject("sender","false");
        }
        modelAndView.addObject("url","messagesOwner");
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);

        return modelAndView;
    }

    @RequestMapping(value={"/messagesRenter"}, method = RequestMethod.GET/*, produces= "application/javascript"*/)
    public ModelAndView messagesRenter(@RequestParam("pageSize") Optional<Integer> pageSize,
                                      @RequestParam("page") Optional<Integer> page){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/messages");
        Authentication authentication = authenticationFacade.getAuthentication();
        if (!authentication.getName().equals("anonymousUser")) {
            modelAndView.addObject("uname", authentication.getName());
            UsersEntity userS = userService.findByUsername(authentication.getName());
            modelAndView.addObject("type", String.valueOf(userS.getType()));
        }
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Pager pager=null;
        RenterEntity renter=userService.findRenterByUsername(authentication.getName());
        Page<MessagesEntity> msg=messagesRepository.findAllByRenter(renter,new PageRequest(evalPage, evalPageSize));

        pager= new Pager(msg.getTotalPages(), msg.getNumber(), BUTTONS_TO_SHOW);
        if(msg.getTotalElements()!=0){
            modelAndView.addObject("pager", pager);
            modelAndView.addObject("items", msg);
            modelAndView.addObject("sender","true");
        }
        modelAndView.addObject("url","messagesRenter");
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);

        return modelAndView;
    }



    @RequestMapping(value="/response/{messageID}", method = RequestMethod.POST)
    public ModelAndView response( @PathVariable String messageID, @RequestParam("response") String Response,@RequestParam("url") String url){
        ModelAndView modelAndView = new ModelAndView();
        Authentication authentication = authenticationFacade.getAuthentication();
        modelAndView.addObject("uname", authentication.getName());
        UsersEntity userS = userService.findByUsername(authentication.getName());
        modelAndView.addObject("type", String.valueOf(userS.getType()));

        Integer messageID_ = Integer.parseInt(messageID);
        MessagesEntity messagesEntity=messagesRepository.findById(messageID_);
        messagesEntity.setResponse(Response);
        messagesRepository.save(messagesEntity);


        System.out.println("responses");


        modelAndView.setViewName("redirect:/"+url);
        return modelAndView;
    }

    @Transactional
    @RequestMapping(value="/remove/{messageID}", method = RequestMethod.POST)
    public ModelAndView remove( @PathVariable String messageID,@RequestParam("url") String url){
        ModelAndView modelAndView = new ModelAndView();
        Authentication authentication = authenticationFacade.getAuthentication();
        modelAndView.addObject("uname", authentication.getName());
        UsersEntity userS = userService.findByUsername(authentication.getName());
        modelAndView.addObject("type", String.valueOf(userS.getType()));

        Integer messageID_ = Integer.parseInt(messageID);
        MessagesEntity messagesEntity=messagesRepository.findById(messageID_);

        messagesRepository.deleteById(messageID_);

        System.out.println("removed");
        modelAndView.setViewName("redirect:/"+url);
        return modelAndView;
    }
}

