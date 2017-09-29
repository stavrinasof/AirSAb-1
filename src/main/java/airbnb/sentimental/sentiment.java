package airbnb.sentimental;

import airbnb.model.*;
import airbnb.repository.ReservationRepository;
import airbnb.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.abs;


/**
 * Created by Σταυρίνα on 28/9/2017.
 */
public class sentiment {

    private  nlp nlp;
    public  sentiment()
    {
        nlp.init();
    }

    public int getsentiment(String comment)
    {

        int retval=this.nlp.findSentiment(comment);
        System.out.println(comment + " : " + retval);
        return retval;
    }
/*
    public LinkedHashSet<ApartmentEntity> getRecommended(RenterEntity renterEntity,ArrayList<UsersEntity> renterList) {
        LinkedHashSet<ApartmentEntity> rec = new LinkedHashSet<ApartmentEntity>();
        Set<ReservationEntity> res = renterEntity.getReservationsByUsersUsername();
      //  ArrayList<UsersEntity> renterList = userService.findAllRenters();

        HashMap<UsersEntity, Integer> to_Rec = new HashMap<UsersEntity, Integer>();
        for (UsersEntity u : renterList) {
            Integer amount = 0;
            ArrayList<ReservationEntity> allu=reservationRepository.findAllByRenter(u.getRenterByUsername());
            for (ReservationEntity ur : allu) {
                for (ReservationEntity myr : res) {
                    if (myr.getApartment().getId() == ur.getApartment().getId()) {
                        amount++;
                    }
                }
            }
            to_Rec.put(u, amount);
        }

        Integer i1 = 0;
        Integer i2 = 0;
        Integer i3 = 0;
        Integer i4 = 0;
        Integer i5 = 0;
        UsersEntity test_user = new UsersEntity();
        HashMap<UsersEntity, Integer> train_set = new HashMap<UsersEntity, Integer>();
        for (HashMap.Entry<UsersEntity, Integer> entry : to_Rec.entrySet()) {
            if (entry.getValue() > i1) {
                i1 = entry.getValue();
                test_user = entry.getKey();
            }
        }
        train_set.put(test_user, i1);
        to_Rec.remove(test_user, i1);
        for (HashMap.Entry<UsersEntity, Integer> entry : to_Rec.entrySet()) {
            if (entry.getValue() > i2) {
                i2 = entry.getValue();
                test_user = entry.getKey();
            }
        }
        train_set.put(test_user, i2);
        to_Rec.remove(test_user, i2);
        for (HashMap.Entry<UsersEntity, Integer> entry : to_Rec.entrySet()) {
            if (entry.getValue() > i3) {
                i2 = entry.getValue();
                test_user = entry.getKey();
            }
        }
        train_set.put(test_user, i3);
        to_Rec.remove(test_user, i3);
        for (HashMap.Entry<UsersEntity, Integer> entry : to_Rec.entrySet()) {
            if (entry.getValue() > i4) {
                i2 = entry.getValue();
                test_user = entry.getKey();
            }
        }
        train_set.put(test_user, i4);
        to_Rec.remove(test_user, i4);
        for (HashMap.Entry<UsersEntity, Integer> entry : to_Rec.entrySet()) {
            if (entry.getValue() > i5) {
                i2 = entry.getValue();
                test_user = entry.getKey();
            }
        }
        train_set.put(test_user, i5);
        to_Rec.remove(test_user, i5);
        int k = 0;

        for (HashMap.Entry<UsersEntity, Integer> entry : train_set.entrySet()) {
            UsersEntity u = entry.getKey();
            ArrayList<ReservationEntity> allu=reservationRepository.findAllByRenter(u.getRenterByUsername());
            for (ReservationEntity ur : allu) {
                for (ReservationEntity bb : res) {
                    Set<CommentsEntity> comments = ur.getApartment().getComments();
                    for (CommentsEntity com : comments) {
                        Set<CommentsEntity> caat = bb.getApartment().getComments();
                        for (CommentsEntity ct : caat) {
                            if ( ((getsentiment(ct.getComment())==getsentiment(com.getComment())) || (abs(ct.getRating()-com.getRating()) <=0.5) )
                                    && !(ur.getApartmentOwner().getUsersUsername()).equals(renterEntity.getUsersUsername())
                                    && ur.getApartment().getId() != bb.getApartment().getId()) {
                                if (k < 6) {
                                    rec.add(ur.getApartment());
                                    k++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return rec;
    }*/
}
