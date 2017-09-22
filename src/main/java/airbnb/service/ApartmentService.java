package airbnb.service;

import airbnb.model.ApartmentEntity;
import airbnb.model.OwnerEntity;
import airbnb.model.RenterEntity;
import airbnb.model.ReservationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;

import java.security.acl.Owner;
import java.util.Date;
import java.util.Optional;


@Service("apartmentService")
public interface ApartmentService {
    public ApartmentEntity findById(Integer id);
    public  void saveApartment(ApartmentEntity apartmentEntity, OwnerEntity ownerEntity,String photograph);
    public ArrayList<String> getFeatures(ApartmentEntity apartmentEntity);
    public String getType(ApartmentEntity apartmentEntity);
    public void updateApartment(ApartmentEntity ap, ApartmentEntity old);
    public Page<ApartmentEntity> findOwnersAparts(OwnerEntity ownerEntity,Pageable pageable);
    public void uploadPhoto(ApartmentEntity ap,String photo);
    public void uploadPhoto2(ApartmentEntity ap,String photo);
    public void uploadPhoto3(ApartmentEntity ap,String photo);
    public void uploadPhoto4(ApartmentEntity ap,String photo);
    public Page<ApartmentEntity> findAparts(Pageable pageable);
    public Page<ApartmentEntity> findAparts(Optional<Integer> heating,Optional<Float>  maxPrice,Optional<Integer>  kitchen,Optional<Integer>  tv,Optional<Integer>  type,Optional<Integer>  elevator,Optional<Integer>  ac,Optional<Integer>  internet,Optional<Integer>  parking,Pageable pageable);
    public Page<ApartmentEntity> findAparts(Optional<String> country,Optional<String> town,Optional<String> area,Optional<String> arrivalDate,Optional<String> departureDate,Optional< Integer> people, Pageable pageable);
    public  int makeReservation(ReservationEntity reservation, ApartmentEntity apart, RenterEntity renter) throws ParseException;
}