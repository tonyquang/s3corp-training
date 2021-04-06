package com.server.trafficweb.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyKey implements Serializable {

	private static final long serialVersionUID = 6533373800387091752L;
	private String userId;
	private String url;
	private String date;
}
