package airbnb.service;

import airbnb.model.*;
import airbnb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Arianna on 29/8/2017.
 */
@Service("apartmentService")
public class ApartmentServiceImpl implements ApartmentService {

    @Autowired
    private ReservedRepository reservedRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private RenterRepository renterRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public ApartmentEntity findById(Integer id){
        return  apartmentRepository.findById(id);
    }

    @Override
    public  void saveApartment(ApartmentEntity apartmentEntity, OwnerEntity ownerEntity,String photograph)
    {
        apartmentEntity.setOwnerByOwner(ownerEntity);
        apartmentEntity.setPhoto(photograph);
        apartmentEntity.setPhoto2("");
        apartmentEntity.setPhoto3("");
        apartmentEntity.setPhoto4("");
        apartmentEntity.setRating((float)0);
        apartmentEntity.setReviews(0);
        apartmentRepository.save(apartmentEntity);
        Set<ApartmentEntity> apartmentEntitySet=ownerEntity.getApartments();
        apartmentEntitySet.add(apartmentEntity);
        ownerRepository.save(ownerEntity);
    }

    @Override
    public ArrayList<String> getFeatures(ApartmentEntity apartmentEntity){
        ArrayList<String> features=new ArrayList<String>();
        if(apartmentEntity.getParking()==1) features.add("Parking");
        if(apartmentEntity.getInternet()==1) features.add("Internet");
        if(apartmentEntity.getAc()==1) features.add("Air Condition");
        if(apartmentEntity.getElevator()==1) features.add("Elevator");
        if(apartmentEntity.getTv()==1) features.add("Tv");
        if(apartmentEntity.getKitchen()==1) features.add("Kitchen");
        if(apartmentEntity.getHeating()==1) features.add("Central Heating");
        if(apartmentEntity.getLivingroom()==1) features.add("Living Room");
        if(apartmentEntity.getPets()==1) features.add("Pets Allowed");
        if(apartmentEntity.getSmoking()==1) features.add("Smoking Allowed");
        if(apartmentEntity.getEvents()==1) features.add("For Events");
        return features;
    }


    @Override
    public String getType(ApartmentEntity apartmentEntity)
    {
        int type=apartmentEntity.getType();
        if(type==0)
            return "Private Residence";
        else if(type==1)
            return "Public Residence";
        else if(type==2)
            return "Whole Residence";
        else
            return "Unknown";
    }



    @Override
    public void updateApartment(ApartmentEntity ap, ApartmentEntity old){
        if(!old.getDescription().replaceAll(" ","").equals("")){
            ap.setDescription(old.getDescription());
        }
        if(!old.getName().replaceAll(" ","").equals(""))
        {
            ap.setName(old.getName());
        }
        if(!old.getCountry().replaceAll(" ","").equals(""))
        {
            ap.setCountry(old.getCountry());
        }
        if(!old.getTown().replaceAll(" ","").equals(""))
        {
            ap.setTown(old.getTown());
        }
        if(!old.getArea().replaceAll(" ","").equals(""))
        {
            ap.setArea(old.getArea());
        }
        if(!old.getStartdate().replaceAll(" ","").equals(""))
        {
            ap.setStartdate(old.getStartdate());
        }
        if(!old.getFinaldate().replaceAll(" ","").equals(""))
        {
            ap.setFinaldate(old.getFinaldate());
        }

        if(!String.valueOf(old.getPrice()).replaceAll(" ","").equals(""))
        {
            ap.setPrice(old.getPrice());
        }
        if(!String.valueOf(old.getFloor()).replaceAll(" ","").equals(""))
        {
            ap.setFloor(old.getFloor());
        }
        if(!String.valueOf(old.getCapacity()).replaceAll(" ","").equals(""))
        {
            ap.setCapacity(old.getCapacity());
        }

        if(!String.valueOf(old.getSpaceArea()).replaceAll(" ","").equals(""))
        {
            ap.setSpaceArea(old.getSpaceArea());
        }

        if(!String.valueOf(old.getBeds()).replaceAll(" ","").equals(""))
        {
            ap.setBeds(old.getBeds());
        }

        if(!String.valueOf(old.getMinimumres()).replaceAll(" ","").equals(""))
        {
            ap.setMinimumres(old.getMinimumres());
        }
        if(!String.valueOf(old.getBaths()).replaceAll(" ","").equals(""))
        {
            ap.setBaths(old.getBaths());
        }
        ap.setParking(old.getParking());
        ap.setInternet(old.getInternet());
        ap.setAc(old.getAc());
        ap.setElevator(old.getElevator());
        ap.setTv(old.getTv());
        ap.setKitchen(old.getKitchen());
        ap.setHeating(old.getHeating());
        ap.setLivingroom(old.getLivingroom());

        ap.setPets(old.getPets());
        ap.setSmoking(old.getSmoking());
        ap.setEvents(old.getEvents());
        ap.setType(old.getType());
        apartmentRepository.save(ap);

    }

    @Override
    public Page<ApartmentEntity> findOwnersAparts(OwnerEntity ownerEntity, Pageable pageable){
              return apartmentRepository.findAllByOwner(ownerEntity,pageable);
    }


    @Override
    public void uploadPhoto(ApartmentEntity ap,String photo)
    {
        ap.setPhoto(photo);
        apartmentRepository.save(ap);
    }

    @Override
    public void uploadPhoto2(ApartmentEntity ap,String photo)
    {
        ap.setPhoto2(photo);
        apartmentRepository.save(ap);
    }

    @Override
    public void uploadPhoto3(ApartmentEntity ap,String photo)
    {
        ap.setPhoto3(photo);
        apartmentRepository.save(ap);
    }

    @Override
    public void uploadPhoto4(ApartmentEntity ap,String photo)
    {
        ap.setPhoto4(photo);
        apartmentRepository.save(ap);
    }

    @Override
    public Page<ApartmentEntity> findAparts(Pageable pageable){
        return apartmentRepository.findAllOrderByPrice(pageable);
    }

    @Override
    public Page<ApartmentEntity> findAparts(Optional<Integer> heating, Optional<Float>  maxPrice, Optional<Integer>  kitchen, Optional<Integer>  tv, Optional<Integer>  type, Optional<Integer>  elevator, Optional<Integer>  ac, Optional<Integer>  internet, Optional<Integer>  parking, Pageable pageable) {
        Page<ApartmentEntity> aparts = null;
        boolean nofilter = true;
        if (heating.isPresent()) {
            aparts = apartmentRepository.findAllByHeating(heating.get(), pageable);
            nofilter = false;
        }

        if (maxPrice.isPresent() && aparts != null) {
            nofilter = false;
            Iterator<ApartmentEntity> list = aparts.iterator();
            while (list.hasNext()) {
                if (list.next().getPrice() > maxPrice.get())
                    list.remove();
            }
        }
        if (maxPrice.isPresent() && aparts == null) {
            nofilter = false;
            aparts = apartmentRepository.findAllByPriceLessThanEqual(maxPrice.get(), pageable);

        }
        if (kitchen.isPresent() && aparts != null) {
            nofilter = false;
            Iterator<ApartmentEntity> list = aparts.iterator();
            while (list.hasNext()) {
                if (list.next().getKitchen() == 0)
                    list.remove();
            }
        }
        if (kitchen.isPresent() && aparts == null) {
            nofilter = false;
            aparts = apartmentRepository.findAllByKitchen(kitchen.get(), pageable);
        }

        if (tv.isPresent() && aparts != null) {
            nofilter = false;
            Iterator<ApartmentEntity> list = aparts.iterator();
            while (list.hasNext()) {
                if (list.next().getTv() == 0)
                    list.remove();
            }
        }
        if (tv.isPresent() && aparts == null) {
            nofilter = false;
            aparts = apartmentRepository.findAllByTv(tv.get(), pageable);
        }
        if (elevator.isPresent() && aparts != null) {
            nofilter = false;
            Iterator<ApartmentEntity> list = aparts.iterator();
            while (list.hasNext()) {
                if (list.next().getElevator() == 0)
                    list.remove();
            }
        }
        if (elevator.isPresent() && aparts == null) {
            nofilter = false;
            aparts = apartmentRepository.findAllByElevator(elevator.get(), pageable);

        }
        if (ac.isPresent() && aparts != null) {
            nofilter = false;
            Iterator<ApartmentEntity> list = aparts.iterator();
            while (list.hasNext()) {
                if (list.next().getAc() == 0)
                    list.remove();
            }
        }
        if (ac.isPresent() && aparts == null) {
            nofilter = false;
            aparts = apartmentRepository.findAllByAc(ac.get(), pageable);

        }
        if (parking.isPresent() && aparts != null) {
            nofilter = false;
            Iterator<ApartmentEntity> list = aparts.iterator();
            while (list.hasNext()) {
                if (list.next().getParking() == 0)
                    list.remove();
            }
        }
        if (parking.isPresent() && aparts == null) {
            nofilter = false;
            aparts = apartmentRepository.findAllByParking(parking.get(), pageable);
        }
        if (type.isPresent() && aparts != null) {
            nofilter = false;
            Iterator<ApartmentEntity> list = aparts.iterator();
            while (list.hasNext()) {
                if (list.next().getType() != type.get())
                    list.remove();
            }
        }
        if (type.isPresent() && aparts == null) {
            aparts = apartmentRepository.findAllByType(type.get(), pageable);
            nofilter = false;
        }
        if (internet.isPresent() && aparts != null) {
            nofilter = false;
            Iterator<ApartmentEntity> list = aparts.iterator();
            while (list.hasNext()) {
                if (list.next().getInternet() == 0)
                    list.remove();
            }
        }
        if (internet.isPresent() && aparts == null)
        {
            nofilter = false;
            aparts = apartmentRepository.findAllByInternet(internet.get(), pageable);
        }
        if(nofilter==true)
            aparts=apartmentRepository.findAll(pageable);
        return aparts;
    }

    @Override
    public Page<ApartmentEntity> findAparts(Optional<String> country,Optional<String> town,Optional<String> area,Optional<String> arrivalDate,Optional<String> departureDate,Optional< Integer> people, Pageable pageable)
    {
        Page<ApartmentEntity> aparts = null;
        boolean nofilter = true;
        if (country.isPresent() && !country.get().replaceAll(" ","").equals("")) {
            aparts = apartmentRepository.findAllByCountry(country.get(), pageable);
            nofilter = false;
        }
        if (town.isPresent() && !town.get().replaceAll(" ","").equals("") && aparts != null) {
            nofilter = false;
            Iterator<ApartmentEntity> list = aparts.iterator();
            while (list.hasNext()) {
                if (!list.next().getTown().equals(town.get()))
                    list.remove();
            }
        }
        if (town.isPresent() && !town.get().replaceAll(" ","").equals("") && aparts == null) {
            nofilter = false;
            aparts = apartmentRepository.findAllByTown(town.get(), pageable);

        }
        if (area.isPresent() && !area.get().replaceAll(" ","").equals("") && aparts != null) {
            nofilter = false;
            Iterator<ApartmentEntity> list = aparts.iterator();
            while (list.hasNext()) {
                if (!list.next().getArea().equals(area.get()))
                    list.remove();
            }
        }
        if (area.isPresent() && !area.get().replaceAll(" ","").equals("") && aparts == null) {
            nofilter = false;
            aparts = apartmentRepository.findAllByArea(area.get(), pageable);
        }

        if (people.isPresent() && aparts != null) {
            nofilter = false;
            Iterator<ApartmentEntity> list = aparts.iterator();
            while (list.hasNext()) {
                if (list.next().getCapacity() < people.get())
                    list.remove();
            }
        }
        if (people.isPresent() && aparts == null) {
            nofilter = false;
            aparts = apartmentRepository.findAllByCapacityIsGreaterThanEqual(people.get(), pageable);
        }
        if (arrivalDate.isPresent() && !arrivalDate.get().replaceAll(" ","").equals("") && aparts != null) {
            nofilter = false;
            Iterator<ApartmentEntity> list = aparts.iterator();
            while (list.hasNext()) {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd", Locale.ENGLISH);
//                LocalDate date = LocalDate.parse(list.next().getStartdate(), formatter);
//                if (date.compareTo(LocalDate.parse(arrivalDate.get(), formatter))>0)
                  if(list.next().getStartdate().compareTo(arrivalDate.get())>0)
                    list.remove();
            }
        }
        if (arrivalDate.isPresent() && !arrivalDate.get().replaceAll(" ","").equals("") && aparts == null) {
            nofilter = false;
            aparts = apartmentRepository.findAllByStartDate(arrivalDate.get(), pageable);
        }
        if (departureDate.isPresent() && !departureDate.get().replaceAll(" ","").equals("") && aparts != null) {
            nofilter = false;
            Iterator<ApartmentEntity> list = aparts.iterator();
            while (list.hasNext()) {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd", Locale.ENGLISH);
//                LocalDate date = LocalDate.parse(list.next().getFinaldate(), formatter);
              //  if (date.compareTo(LocalDate.parse(departureDate.get(), formatter))<0)
                if(list.next().getFinaldate().compareTo(departureDate.get())<0)
                    list.remove();
            }
        }
        if (departureDate.isPresent() && !departureDate.get().replaceAll(" ","").equals("") && aparts == null) {
            nofilter = false;
            aparts = apartmentRepository.findAllByFinalDate(departureDate.get(), pageable);

        }
        if(nofilter==true)
            aparts=apartmentRepository.findAll(pageable);
        return aparts;
    }

    @Override
    public  int makeReservation(ReservationEntity reservation, ApartmentEntity apart, RenterEntity renter) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String startDate=apart.getStartdate();
        String finalDate=apart.getFinaldate();
        String arrivalDate=reservation.getStartdate();
        String departureDate=reservation.getFinaldate();

        Date date = formatter.parse(arrivalDate);
        Date date2=formatter.parse(departureDate);
        Date dates= formatter.parse(startDate);
        Date datef= formatter.parse(finalDate);

        Set<ReservedEntity> reservedDays=apart.getReservedEntities(); //days occupied
        System.out.println("To diamerisma einai kleismeno tis meres "+reservedDays);
        if(date2.after(date)&& (date2.before(datef)|| date2.compareTo(datef)==0)&& (date.after(dates)|| date.compareTo(dates)==0)){
            System.out.println("To diastima einai apodekto");
            int diff = date2.getDate()- date.getDate();
            TimeUnit timeUnit = TimeUnit.DAYS;
            System.out.print(timeUnit.convert(diff, TimeUnit.DAYS));
            List<String> dateslist=new ArrayList<String>();     //the days to save as reserved
            dateslist.add(startDate);
            for(int i=1;i<diff;i++)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                System.out.println(formatter.format(calendar.getTime()));
                date=calendar.getTime();
                if(reservedDays.contains(formatter.format(calendar.getTime())))
                    return -2;
                dateslist.add(formatter.format(calendar.getTime()));
            }
            System.out.println("Tha meinw tis meres "+dateslist);

            /*Save the reservation*/
            reservation.setApartment(apart);
            reservation.setRenterUsersUsername(renter);
            reservation.setApproval(0);
            reservation.setApartmentOwner(apart.getOwner());
            System.out.println("Ty to save reservation for apart" + apart.getId() + ", with owner " + apart.getOwner());
            reservationRepository.save(reservation);

            /*Save renter's new Reservation*/
            Set<ReservationEntity> reservationEntitySetOfRenter = renter.getReservationsByUsersUsername();// renter's reservations
            reservationEntitySetOfRenter.add(reservation);
            renterRepository.save(renter);

            System.out.println("The days reserved before "+reservedDays);
            /*Save the "new" Reserved Days */
            for(int i=0;i<dateslist.size();i++)
            {
                ReservedEntity reservedEntity=new ReservedEntity(dateslist.get(i),apart);
                reservedDays.add(reservedEntity);
                System.out.println("Tha apothikeusw tin imera "+reservedEntity.getDate());
                reservedRepository.save(reservedEntity);
            }
            System.out.println("The days reserved now "+reservedDays);
            apart.setReservedEntities(reservedDays);
            apartmentRepository.save(apart);
            return 1;
        }
        else
            return -1;
    }

    @Override
    public void removephoto(ApartmentEntity apartmentEntity, Integer num){
        if(num==1)
            apartmentEntity.setPhoto("");
        else if(num==2)
            apartmentEntity.setPhoto2("");
        else if(num==3)
            apartmentEntity.setPhoto3("");
        else if(num==4)
            apartmentEntity.setPhoto4("");

        apartmentRepository.save(apartmentEntity);
        return;
    }
}
