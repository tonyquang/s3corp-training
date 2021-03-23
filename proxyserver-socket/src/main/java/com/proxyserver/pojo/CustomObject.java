package com.proxyserver.pojo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
}
