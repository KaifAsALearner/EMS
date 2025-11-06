package five.ind.ems_mvc.controller;

import five.ind.ems_mvc.entity.Asset;
import five.ind.ems_mvc.entity.AssetAssigned;
import five.ind.ems_mvc.entity.Employee;
import five.ind.ems_mvc.entity.compositeId.AssetAssignedId;
import five.ind.ems_mvc.entity.enums.AssetStatus;
import five.ind.ems_mvc.service.AssetAssignedService;
import five.ind.ems_mvc.service.AssetService;
import five.ind.ems_mvc.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetAssignedService assetAssignedService;

    @Autowired
    private EmployeeService employeeService; // You must have this service

    @GetMapping("/all")
    public String listAssets(Principal principal,Model model) {
        String username = principal.getName();
        Employee user = employeeService.findByUsername(username);
        boolean isManager = user.getRole().getId().getRoleName().equalsIgnoreCase("MANAGER");
        List<Asset> assets = assetService.getAllAssets();
        model.addAttribute("assets", assets);
        model.addAttribute("isManager", isManager);
        return "assets-list";
    }

    @GetMapping("/new")
    public String newAssetForm(Model model) {
        model.addAttribute("asset", new Asset());
        model.addAttribute("statuses", AssetStatus.values());
        return "asset-form";
    }

    @PostMapping("/new")
    public String createAsset(@ModelAttribute Asset asset) {
        assetService.createAsset(asset);
        return "redirect:/assets/all";
    }

    @GetMapping("/{assetId}/edit")
    public String editAssetForm(@PathVariable Long assetId, Model model) {
        Asset asset = assetService.getAssetById(assetId);
        model.addAttribute("asset", asset);
        model.addAttribute("statuses", AssetStatus.values());
        return "asset-form";
    }

    @PostMapping("/{assetId}/edit")
    public String updateAsset(@PathVariable Long assetId, @ModelAttribute Asset asset) {
        assetService.updateAsset(assetId, asset);
        return "redirect:/assets/all";
    }

    @GetMapping("/{assetId}/assign")
    public String assignAssetForm(@PathVariable Long assetId, Model model) {
        Asset asset = assetService.getAssetById(assetId);
        List<Employee> employees = employeeService.findAllEmployees();
        model.addAttribute("asset", asset);
        model.addAttribute("employees", employees);
        return "asset-assign-form";
    }

    @PostMapping("/{assetId}/assign")
    public String assignAsset(@PathVariable Long assetId,
                              @RequestParam Long empId,
                              @RequestParam String issuedOn, // From form input
                              @RequestParam String remark) { // From form input
        AssetAssigned assignment = new AssetAssigned();
        assignment.setAsset(assetService.getAssetById(assetId));
        assignment.setEmployee(employeeService.findById(empId));
        assignment.setIssuedOn(LocalDate.parse(issuedOn));
        assignment.setRemark(remark);
        // Setup composite key:
        AssetAssignedId id = new AssetAssignedId();
        id.setAssetId(assetId);
        id.setEmpId(empId);
        assignment.setId(id);
        assetAssignedService.assignAsset(assignment);

        // Optionally update asset status:
        Asset asset = assetService.getAssetById(assetId);
        asset.setStatus(AssetStatus.ASSIGNED);
        assetService.updateAsset(assetId, asset);

        return "redirect:/assets/all";
    }

    @GetMapping("/assigned")
    public String employeeAssets(Principal principal, Model model) {
        Employee employee = employeeService.findByUsername(principal.getName());
        List<AssetAssigned> assigned = assetAssignedService.getAssetsByEmployee(employee.getEmpId());
        model.addAttribute("assignedAssets", assigned);
        return "employee-assets";
    }
}