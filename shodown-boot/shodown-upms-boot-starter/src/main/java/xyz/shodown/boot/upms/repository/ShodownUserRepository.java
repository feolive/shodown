package xyz.shodown.boot.upms.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xyz.shodown.boot.upms.entity.ShodownUser;

/**
 * @description: USER REPO
 * @author: wangxiang
 * @date: 2022/4/23 17:19
 */
@Repository
public interface ShodownUserRepository extends JpaRepository<ShodownUser,Long> {

    /**
     * 根据token和mark查询用户信息
     * @param token token
     * @param mark 有效标识 0无效 1有效
     * @return 用户信息
     */
    ShodownUser findByTokenAndMark(String token,Integer mark);

    /**
     * 根据用户id查找用户
     * @param userId 用户id
     * @param mark 有效标识 0无效 1有效
     * @return 用户信息
     */
    ShodownUser findByUserIdAndMark(String userId,Integer mark);

    /**
     * 根据email查找用户
     * @param email 邮箱
     * @param mark 有效标识 0无效 1有效
     * @return 用户信息
     */
    ShodownUser findByEmailAndMark(String email,Integer mark);

    /**
     * 根据手机号查询用户
     * @param mobile 手机号
     * @param mark 有效标识 0无效 1有效
     * @return 用户信息
     */
    ShodownUser findByMobileAndMark(String mobile,Integer mark);

    @Modifying
    @Query("update ShodownUser s set s.token=null,s.updateTime=current_timestamp where s.token = ?1")
    void removeToken(String token);

    @Modifying
    @Query("update ShodownUser s set s.secretKey = ?2,s.updateTime=current_timestamp where s.userId = ?1")
    void saveSecretKey(String userId,String secretKey);
}
