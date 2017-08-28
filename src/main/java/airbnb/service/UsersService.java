package airbnb.service;

import airbnb.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("usersService")
public interface UsersService {
    public UsersEntity findByUsername(String username);
    public OwnerEntity findOwnerByUsername(String username);

   public void saveUser(UsersEntity user);
   // public void saveUserProvider(UserEntity user, ProviderEntity provider);
 //  public void saveUserOwner(UsersEntity user, OwnerEntity owner);
    public UsersEntity findByUsernamePassword(String username, String Password);

    public void updateUser(UsersEntity useron,UsersEntity user);
    public Page<UsersEntity> findAllPageable(Pageable pageable);
    public Page<UsersEntity> findAllRenters(Pageable pageable);
    public Page<UsersEntity> findAllOwners(Pageable pageable);
    public Page<OwnerEntity> findAllnotApproved(Pageable pageable);
    //public void updateUserProvider(ProviderEntity provideron,ProviderEntity provider,UserEntity useron,UserEntity user);
     public void approveOwner(OwnerEntity ownerEntity);
   // public void uploadPhoto(ParentEntity parenton,ParentEntity parent,UserEntity useron,UserEntity user,String photopath);
}