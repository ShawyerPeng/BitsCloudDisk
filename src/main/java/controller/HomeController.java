package controller;

import constant.MenuConsts;
import service.dto.UserFileDTO;
import service.dto.UserFolderDTO;
import service.dto.UserDTO;
import model.UserFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import controller.reqbody.MoveReqBody;
import controller.reqbody.RenameFileReqBody;
import controller.reqbody.RenameFolderReqBody;
import controller.reqbody.ShredReqBody;
import service.DiskService;
import service.UserService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(produces = "application/json", consumes = "application/json")
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private DiskService diskService;

    /**
     * 功能：根据用户名获取完整的用户信息
     * 示例：GET users/admin，获取admin用户的信息
     */
    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public UserDTO getUserInfo(@PathVariable String username) {
        // 参数已经经过拦截器校验，所以不需要再检查参数
        return userService.getUserByUsername(username);
    }

    /**
     * 功能：获取用户不同菜单下的文件（及文件夹）列表
     * 示例：GET users/admin/photo，获取admin用户的所有照片
     */
    @RequestMapping(value = "/users/{username}/{menu}", method = RequestMethod.GET)
    public Map<String, Object> getMenu(@PathVariable String username, @PathVariable String menu) {
        // 检查menu参数是否合法
        if (!MenuConsts.getConsts().contains(menu)) {
            throw new IllegalArgumentException("Illegal argument: " + menu);
        }

        int userId = userService.getUserByUsername(username).getUserId();
        return diskService.getMenuContents(userId, menu);
    }

    /**
     * 功能：获取某个文件夹的内容
     * 示例：GET users/admin/disk/folders/55?sort=1，获取admin用户网盘内folderID=55的文件夹内容，以最后修改时间排序
     *
     * @param sort sort=1按时间排序，sort=其它值 按中文拼音排序
     */
    @RequestMapping(value = "/users/{username}/disk/folders/{folderId}", method = RequestMethod.GET)
    public Map<String, Object> getFolderContents(
            @PathVariable String username, @PathVariable Integer folderId, @RequestParam(defaultValue = "0") int sort) {
        // TODO 参数校验
        int userId = userService.getUserByUsername(username).getUserId();
        return diskService.getFolderContents(userId, folderId, sort);
    }

    /**
     * 功能：搜索用户网盘内的文件
     * 示例：GET users/admin/disk/search?input=txt，搜索admin用户网盘内文件名含“txt”的文件及文件夹
     */
    @RequestMapping(value = "/users/{username}/disk/search", method = RequestMethod.GET)
    public Map<String, Object> search(@PathVariable String username, @RequestParam("input") String input) {
        int userId = userService.getUserByUsername(username).getUserId();
        Map<String, Object> result = diskService.search(userId, input);
        return result;
    }

    /**
     * 功能：批量移动文件及文件夹
     * 示例：PATCH users/admin/disk/move，请求体：{"files":[1,2],"folders":[3,4],"dest":5}，
     * 将ID=1,2的文件及ID=3,4的文件夹移动到ID=5的文件夹中
     */
    @RequestMapping(value = "/users/{username}/disk/move", method = RequestMethod.PATCH)
    public Map<String, Object> move(@PathVariable String username, @RequestBody @Valid MoveReqBody reqBody) {
        // int userId = userService.getUser(username).getId();
        // TODO 参数校验
        return diskService.move(reqBody.getFolders(), reqBody.getFiles(), reqBody.getDest());
    }

    /**
     * 功能：重命名文件夹
     * 示例：PATCH users/admin/disk/folders/123，请求体：{"folderName":"rename_test"}，
     * 把ID=123的文件夹重命名为rename_test
     */
    @RequestMapping(value = "/users/{username}/disk/folders/{folderId}", method = RequestMethod.PATCH)
    public UserFolderDTO renameFolder(@PathVariable String username, @PathVariable int folderId
            , @RequestBody @Valid RenameFolderReqBody reqBody) {
        // int userId = userService.getUser(username).getId();
        // TODO 参数校验
        return diskService.renameFolder(folderId, reqBody.getFolderName());
    }

    /**
     * 功能：重命名文件
     * 示例：PATCH users/admin/disk/files/123，请求体：{"fileName":"rename","fileType":"jpg"}，
     * 把ID=123的文件重命名为rename.jpg
     */
    @RequestMapping(value = "/users/{username}/disk/files/{fileId}", method = RequestMethod.PATCH)
    public UserFileDTO renameFile(@PathVariable String username, @PathVariable int fileId
            , @RequestBody @Valid RenameFileReqBody reqBody) {
        // int userId = userService.getUser(username).getId();
        // TODO 参数校验
        return diskService.renameFile(fileId, reqBody.getFileName(), reqBody.getFileType());
    }

    /**
     * 功能：新建文件夹
     * 示例：POST users/admin/disk/folders，请求体：{"folderName":"新建文件夹","parentId":5}，
     * 在ID=5的文件夹下新建一个名字为“新建文件夹”的文件夹
     */
    @RequestMapping(value = "/users/{username}/disk/folders/new", method = RequestMethod.POST)
    public UserFolderDTO newFolder(@PathVariable String username, @RequestBody @Valid UserFolder reqBody) {
        // TODO 参数校验
        int userId = userService.getUserByUsername(username).getUserId();
        reqBody.setUserId(userId);

        return diskService.newFolder(reqBody);
    }

    /**
     * 功能：彻底删除回收站的文件
     * 示例：DELETE users/admin/recycle，请求体：{"files":[11,22],"folders":[33,44]}，
     * 删除ID=11,22的文件和ID=33,44的文件夹，并更新用户已用空间
     */
    @RequestMapping(value = "/users/{username}/recycle", method = RequestMethod.DELETE)
    public UserDTO shred(@PathVariable String username
            , @RequestBody @Valid ShredReqBody reqBody) {
        int userId = userService.getUserByUsername(username).getUserId();
        // TODO 参数校验
        return diskService.shred(reqBody.getFolders(), reqBody.getFiles(), userId);
    }
}