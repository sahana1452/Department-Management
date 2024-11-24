package com.spring.exercisethree.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.exercisethree.dto.DepartmentDto;

@Controller
public class DepartmentController {
	private static final String BASE_URL = "http://localhost:8084/api/department"; 
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	@GetMapping("/getAll")
	public String displayDepartments(Model model) throws Exception{
		
			String url = BASE_URL+"/"+"findAll";
			String response = restTemplate.getForObject(url, String.class);
			System.err.println(response);
			
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(response);
			JsonNode dataNode = rootNode.get("data");
			
			List<Map<String,Object>> department = objectMapper.convertValue(dataNode, List.class);
			model.addAttribute("departments", department);
			return "departments";
	}
	
	@GetMapping("/department/{id}")
	public String getDepartmentById(@PathVariable int id, Model model) throws Exception{
		String url = BASE_URL+"/findById/"+id;
		String response = restTemplate.getForObject(url, String.class);
		System.out.println(response);
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(response);
		JsonNode dataNode = rootNode.get("data");
		
		Map<String,Object> department = objectMapper.convertValue(dataNode, Map.class);
		model.addAttribute("department",department);
		return "edit-department";
	}

    
	@PostMapping("/update/department/{id}")
	public String updateDepartment(@ModelAttribute DepartmentDto departmentDto,@PathVariable int id) {
		String url = BASE_URL+"/"+"update/department/"+id;
		restTemplate.put(url,departmentDto);
		return "redirect:/getAll";
	}

	
    @GetMapping("/addDepartment")
    public String showAddDepartmentForm(Model model) {
        model.addAttribute("department", new DepartmentDto());
        return "add-department";
    }

    // Handle the POST request to add a new department
	/*
	 * @PostMapping("/addDepartment") public String addDepartment(@ModelAttribute
	 * DepartmentDto departmentDto, Model model) { String url = BASE_URL +
	 * "/addDepartment"; restTemplate.postForObject(url, departmentDto,
	 * DepartmentDto.class); return "redirect:/latest"; }
	 */
    
    
    @PostMapping("/addDepartment")
    public String addDepartment(@ModelAttribute DepartmentDto departmentDTO,  Model model) {
        System.out.println("Department Added: " + departmentDTO);
        model.addAttribute("departmentDTO", departmentDTO);
        return "redirect:/latest-department";
    }

    @GetMapping("/latest")
    public String displayLatestDepartments(Model model) throws Exception {
        String url = BASE_URL + "/findLatest";
        String response = restTemplate.getForObject(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode dataNode = rootNode.get("data");

        List<Map<String, Object>> latestDepartments = objectMapper.convertValue(dataNode, List.class);
        model.addAttribute("latestDepartments", latestDepartments); // Updated key
        return "latest-department";
    }

}

