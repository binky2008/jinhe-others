package address.resolution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class GoogleTranslator {
    
    static Logger log = Logger.getLogger(GoogleTranslator.class);
    
    static String RESULT_FILE_DIR = "D:/temp/address/en";
    
    static String RESULT_FILE_PATH() { return RESULT_FILE_DIR + "/result.data"; }
    static String RESULT_KEY_FILE_PATH() { return RESULT_FILE_DIR + "/result.key"; }
    static String ADDR_MAPPED_PATH() { return RESULT_FILE_DIR + "/mapped.data"; }
    
    static Set<String> keySet = Collections.synchronizedSet(new HashSet<String>());
    static String REPEAT_ADDR_TAG = "exsits";
    
    static void init() {
    	File resultKeyFile = new File(RESULT_KEY_FILE_PATH());
    	if( !resultKeyFile.exists() ) {
    		resultKeyFile.getParentFile().mkdirs();
    	} 
    	else {
    		try {
                InputStream inputStream = new FileInputStream(resultKeyFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String data = null;
                while((data = br.readLine()) != null) {
                	keySet.add(data); 
                }
                br.close();
            } catch (Exception e) {
                log.error("读取rusult key文件到内存时出错。", e);
            } 
    	}
    }
    
    static void clear() {
        keySet.clear();
    }
 
	public static String translate(String addressCN, boolean record) {
		if( addressCN == null) return null;
		
		String addrCode = String.valueOf(addressCN.hashCode());
		if(keySet.contains(addrCode)) {
			return REPEAT_ADDR_TAG; // 对于重复的地址不再多次翻译
		}
		
	    String urlEncodedAddress;
        try {
            urlEncodedAddress = URLEncoder.encode(addressCN, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        	throw new RuntimeException("对中文地址进行URLEncoder时异常", e);
        }
        
        String googleURI = "https://translate.google.com.hk/translate_a/single?client=t&sl=zh-CN&tl=en&hl=zh-CN&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&dt=at&ie=UTF-8&oe=UTF-8&otf=1&ssel=6&tsel=3&kc=1&tk=524025|717846" +
        		"&q=" + urlEncodedAddress;
        
        HttpClient client = new HttpClient(); 
        GetMethod method = new GetMethod(googleURI);
        method.setRequestHeader("REQUEST-TYPE", "application/json; charset=UTF-8");
	    
        try {
            client.executeMethod(method);
            int statusCode = method.getStatusCode();

            InputStream responseBodyAsStream = method.getResponseBodyAsStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(responseBodyAsStream, "UTF-8"));
            
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
            	buffer.append(line);
            }
            String responseContent = buffer.toString();
            log.debug("google translate response content: " + responseContent);
            
            if (statusCode == HttpStatus.SC_OK) {
            	if( record ) {
            		int areaHash = addressCN.substring(0, 3).hashCode();
    				output2File( addrCode + " -.- " + responseContent, RESULT_FILE_PATH() + "." + areaHash);
                	output2File( addrCode, RESULT_KEY_FILE_PATH() );
                	keySet.add( addrCode );
            	}
            	
            	// 定时休息以下，以免流量异常被Google拉入黑名单
            	if( keySet.size() > 0 && keySet.size() % 10000 == 0) {
            		Thread.sleep(1000 * 30);
            	}
            	
                int beginIndex = responseContent.indexOf("\"") + 1;
                int endIndex   = responseContent.indexOf("\"", beginIndex);
                if( endIndex > beginIndex ) {
                    String result = responseContent.substring(beginIndex, endIndex);
                    log.debug("google translate result: " + result);
                    return result;
                }
            } 
            else {
                log.error("请求连接失败");
            }
            return null;
        } catch(Exception e) {
            throw new RuntimeException("调用Google翻译时异常", e);
        } finally {
            method.releaseConnection();
        }
	}

    static synchronized void output2File(Object content, String filePath) {
		BufferedWriter bw = null;
		try {
			OutputStream outputStream = new FileOutputStream(filePath, true);
			bw = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));  
			bw.write(content.toString());
			bw.newLine();
			bw.flush();  
		} catch (Exception e) {
			throw new RuntimeException("写入翻译结果到文件时出错", e);
		} finally {
			try {
				if(bw != null) {
					bw.close();
				}
			} catch (IOException e) {
			}
		}
	}
    
    public static void translateAddrsInFile(String addressDataFilePath) {
        File addressDataFile = new File(addressDataFilePath);
        
        GoogleTranslator.RESULT_FILE_DIR = addressDataFile.getParent();
        GoogleTranslator.init();
        GoogleTranslator.log.setLevel(Level.INFO);
        
        List<Address> addressList = Resolution.getAddressList(addressDataFile.getPath());
        ThreadPool.COUNT = 0;
        for(final Address address : addressList) {
            ThreadPool.getInstance().excute(new Task() {
                public void excute() {
                    GoogleTranslator.translate(address.addressCN, true);
                    
                    ThreadPool.addCount();
                    if ((ThreadPool.COUNT > 0 && ThreadPool.COUNT % 100 == 0)) {
                        log.info("已翻译【" + ThreadPool.COUNT + "】个地址。");
                    }
                }
            });
        }
        
        // 等待多线程完成
        while(ThreadPool.COUNT < addressList.size()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
