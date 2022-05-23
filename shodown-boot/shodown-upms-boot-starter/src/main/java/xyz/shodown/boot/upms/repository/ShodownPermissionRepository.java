package xyz.shodown.boot.upms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xyz.shodown.boot.upms.entity.ShodownPermission;
import xyz.shodown.boot.upms.entity.ShodownRole;

import java.util.List;

/**
 * @description: 权限Repo
 * @author: wangxiang
 * @date: 2022/4/24 15:04
 */
@Repository
public interface ShodownPermissionRepository extends JpaRepository<ShodownPermission,Long> {

    /**
     * 根据角色、权限类别、有效标识查询权限信息
     * @param roles 角色列表
     * @param type 权限类别
     * @param mark 有效标识 0无效 1有效
     * @return 权限信息
     */
    @Query("select p from ShodownPermission p inner join p.roles r where r.roleId in(?1) and p.type=?2 and p.mark=?3")
    List<ShodownPermission> findAllByRolesInAndTypeAndMark(List<String> roles,Integer type,Integer mark);

    /**
     * 根据Url地址查询接口权限
     * @param route URL接口地址
     * @param type 权限类别
     * @param mark 有效标识 0无效 1有效
     * @return 权限信息
     */
    @Query("select s from ShodownPermission s where s.route=?1 and s.type=?2 and s.mark=?3")
    ShodownPermission findByRouteAndTypeAndMark(String route,Integer type,Integer mark);
}
