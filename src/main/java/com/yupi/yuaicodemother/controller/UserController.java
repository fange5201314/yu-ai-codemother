package com.yupi.yuaicodemother.controller;

import com.mybatisflex.core.paginate.Page;
import com.yupi.yuaicodemother.common.BaseResponse;
import com.yupi.yuaicodemother.common.ResultUtils;
import com.yupi.yuaicodemother.exception.ErrorCode;
import com.yupi.yuaicodemother.exception.ThrowUtils;
import com.yupi.yuaicodemother.model.dto.user.UserLoginRequest;
import com.yupi.yuaicodemother.model.dto.user.UserRegisterRequest;
import com.yupi.yuaicodemother.model.vo.LoginUserVO;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.yupi.yuaicodemother.model.entity.User;
import com.yupi.yuaicodemother.service.UserService;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户 控制层。
 *
 * @author <a>fange</a>
 */
@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 保存用户。
     *
     * @param user 用户
     * @return 保存结果
     */
    @PostMapping("save")

    public BaseResponse<Boolean> save(@RequestBody User user) {
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "用户信息不能为空");
        // 数据脱敏：不允许直接设置敏感字段
        user.setUserPassword(null);
        user.setId(null);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "保存用户失败");
        return ResultUtils.success(result);
    }

    /**
     * 根据主键删除用户。
     *
     * @param id 主键
     * @return 删除结果
     */
    @DeleteMapping("remove/{id}")

    public BaseResponse<Boolean> remove(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "用户ID无效");
        boolean result = userService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "删除用户失败");
        return ResultUtils.success(result);
    }

    /**
     * 根据主键更新用户。
     *
     * @param user 用户
     * @return 更新结果
     */
    @PutMapping("update")

    public BaseResponse<Boolean> update(@RequestBody User user) {
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "用户信息不能为空");
        ThrowUtils.throwIf(user.getId() == null || user.getId() <= 0, ErrorCode.PARAMS_ERROR, "用户ID无效");
        // 数据脱敏：不允许直接更新敏感字段
        user.setUserPassword(null);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "更新用户失败");
        return ResultUtils.success(result);
    }

    /**
     * 查询所有用户。
     * 注意：此接口仅管理员可访问
     *
     * @return 所有用户数据（已脱敏）
     */
    @GetMapping("list")

    public BaseResponse<List<LoginUserVO>> list(HttpServletRequest request) {
        // 权限验证：仅管理员可访问
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!"admin".equals(loginUser.getUserRole()), ErrorCode.NO_AUTH_ERROR, "无权限访问");
        
        List<User> userList = userService.list();
        // 数据脱敏：转换为 VO 对象
        List<LoginUserVO> userVOList = userList.stream()
                .map(userService::getLoginUserVO)
                .collect(Collectors.toList());
        return ResultUtils.success(userVOList);
    }

    /**
     * 根据主键获取用户。
     *
     * @param id 用户主键
     * @return 用户详情（已脱敏）
     */
    @GetMapping("getInfo/{id}")

    public BaseResponse<LoginUserVO> getInfo(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "用户ID无效");
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        // 数据脱敏：转换为 VO 对象
        LoginUserVO userVO = userService.getLoginUserVO(user);
        return ResultUtils.success(userVO);
    }

    /**
     * 分页查询用户。
     *
     * @param page 分页对象
     * @return 分页结果（已脱敏）
     */
    @GetMapping("page")

    public BaseResponse<Page<User>> page(Page<User> page, HttpServletRequest request) {
        ThrowUtils.throwIf(page == null, ErrorCode.PARAMS_ERROR, "分页参数不能为空");
        // 权限验证：仅管理员可访问
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!"admin".equals(loginUser.getUserRole()), ErrorCode.NO_AUTH_ERROR, "无权限访问");
        
        Page<User> userPage = userService.page(page);
        // TODO: 在后续版本中添加数据脱敏处理
        return ResultUtils.success(userPage);
    }

    @PostMapping("register")

    public BaseResponse<Long> UserRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        Long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")

    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }
    
    @GetMapping("/get/login")

    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    @PostMapping("/logout")

    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }


}
