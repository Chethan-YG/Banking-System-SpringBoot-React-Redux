package com.bank.system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.system.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByFirstnameOrEmail(String first_name, String email);
	Optional<User> findByEmail(String email);
	
	@Query(value="SELECT email FROM user WHERE email = :email", nativeQuery = true)
    String getUserEmail(@Param("email")String email);

    @Query(value="SELECT password FROM user WHERE email = :email", nativeQuery = true)
    String getUserPassword(@Param("email")String email);

    @Query(value="SELECT verified FROM user WHERE email = :email", nativeQuery = true)
    int isVerified(@Param("email")String email);

    @Query(value="SELECT * FROM user WHERE email = :email", nativeQuery = true)
    User getUserDetails(@Param("email")String email);

    @Modifying
    @Query(value = "INSERT INTO user (first_name, last_name, email, password, create_at, verified, token, code) " +
            "VALUES (:first_name, :last_name, :email, :password, CURRENT_TIMESTAMP, 0, :token, :code)", nativeQuery = true)
    @Transactional
    void registerUser(@Param("first_name") String first_name,  
                      @Param("last_name") String last_name,
                      @Param("email") String email,
                      @Param("password") String password,
                      @Param("token") String token,
                      @Param("code") String code);


    @Modifying
    @Query(value = "UPDATE user SET token=null,code=null, verified=1, verified_at=Now(), updated_at=Now() WHERE "+
            "token= :token AND code= :code", nativeQuery = true)
    @Transactional
    void verifyAccount(@Param("token")String token, @Param("code")String code);

    @Query(value = "SELECT token FROM user WHERE token = :token",nativeQuery = true)
    String checkToken(@Param("token")String token);
}
