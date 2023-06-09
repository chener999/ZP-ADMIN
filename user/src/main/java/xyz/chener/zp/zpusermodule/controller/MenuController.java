package xyz.chener.zp.zpusermodule.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.chener.zp.common.config.opLog.annotation.OpLog;
import xyz.chener.zp.common.config.unifiedReturn.annotation.UnifiedReturn;
import xyz.chener.zp.common.utils.DemonstrationSystemUtils;
import xyz.chener.zp.zpusermodule.config.oplog.OpRecordMybatisWrapper;
import xyz.chener.zp.zpusermodule.config.oplog.entity.OpEnum;
import xyz.chener.zp.zpusermodule.entity.UiRouting;
import xyz.chener.zp.zpusermodule.entity.dto.MenuNameDto;
import xyz.chener.zp.zpusermodule.service.MenuService;
import xyz.chener.zp.zpusermodule.service.PermissionService;
import xyz.chener.zp.zpusermodule.service.impl.UiRoutingServiceImpl;

import java.util.List;

/**
 * @Author: chenzp
 * @Date: 2023/02/13/15:38
 * @Email: chen@chener.xyz
 */

@RestController
@UnifiedReturn
@Slf4j
@RequestMapping("/api/web")
@Validated
public class MenuController {

    private final MenuService menuService;
    private final UiRoutingServiceImpl uiRoutingService;
    private final PermissionService permissionService;

    public MenuController(MenuService menuService, UiRoutingServiceImpl uiRoutingService, PermissionService permissionService) {
        this.menuService = menuService;
        this.uiRoutingService = uiRoutingService;
        this.permissionService = permissionService;
    }


    @GetMapping("/getAllMenusName")
    @PreAuthorize("hasAnyRole('menu_list_query')")
    public List<MenuNameDto> getAllMenusName() {
        return menuService.getAllMenuName();
    }


    @GetMapping("/getMenuInfo")
    @PreAuthorize("hasAnyRole('menu_list_query')")
    public UiRouting getMenuInfo(@RequestParam Integer id) {
        return uiRoutingService.lambdaQuery().eq(UiRouting::getId, id).one();
    }

    @PostMapping("/saveMenuInfo")
    @PreAuthorize("hasAnyRole('menu_list_query')")
    @OpLog(operateName = OpEnum.UPDATEMENU,recordClass = OpRecordMybatisWrapper.class )
    public UiRouting saveMenuInfo(@ModelAttribute UiRouting uiRouting) {
        DemonstrationSystemUtils.ban();
        UiRouting res = uiRoutingService.saveOrUpdate(uiRouting) ? uiRouting : null;
        if (res != null){
            permissionService.flushUiPermission();
        }
        return res;
    }

    @PostMapping("/deleteMenuInfo")
    @PreAuthorize("hasAnyRole('menu_list_query')")
    @OpLog(operateName = OpEnum.DELETEMENU,recordClass = OpRecordMybatisWrapper.class )
    public Boolean deleteMenuInfo(@RequestParam Integer id) {
        DemonstrationSystemUtils.ban();
        boolean res = uiRoutingService.removeById(id);
        if (res){
            permissionService.flushUiPermission();
        }
        return res;
    }



    @GetMapping("/getAllParentMenus")
    @PreAuthorize("hasAnyRole('menu_list_query')")
    public List<UiRouting> getAllParentMenus() {
        return uiRoutingService.lambdaQuery().select(UiRouting::getId, UiRouting::getMetaTitle).list();
    }


}
