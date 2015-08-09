/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dao.Group;

@Controller
@Description("A controller for handling requests for group messages")
@EnableAutoConfiguration
public class GroupController {

	@RequestMapping(value = "/groups", method = RequestMethod.GET)
	@ResponseBody
	public List<Group> groups(@RequestParam String input) {
		List<Group> groupList = new ArrayList<Group>();
		groupList.add(new Group("154188317997936", "UF! Ride board"));
		groupList.add(new Group("370655192974313",
				"FSU - USF - UCF - UF Carpool"));
		groupList.add(new Group("388163574548799", "UF Rides"));
		groupList.add(new Group("281301421940210", "UF to MIAMI Ride Board"));
		return groupList;
	}

	@RequestMapping(value = "/getLocationAndDate", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getLocationAndDate(@RequestParam String input) {
		String locations = null;
		String dates = null;
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(new String[]{"/home/mebin/Desktop/Lawrence/informationExtraction1.py", input});
			int exitVal = proc.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String aux;
			if((aux=reader.readLine())!= null)
				locations = aux;
			if((aux=reader.readLine())!= null)
				dates = aux;
			System.out.println("Process exitValue: " + exitVal);
			reader.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		List<String> locationAndDate = new ArrayList<String>();
		locations = locations.replace("[", "");
		locations = locations.replace("]", "");
		locations = locations.replace("u'", "");
		locations = locations.replace("'", "");
		String[] split = locations.split(",");
		locationAndDate.addAll(Arrays.asList(split));
		locationAndDate.add(dates);
		return locationAndDate;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(GroupController.class, args);
	}
}
