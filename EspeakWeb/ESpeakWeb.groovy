
 
 preferences {
    input("Ip", "string", title:"IP of TTS server",required:true, displayDuringSetup: true)
        
    input("Port", "string", title:"Port of TTS server", required:true, displayDuringSetup: true, defaultValue: "80")
        
    input("Path", "string", title: "Path on TTS server", required: true, displayDuringSetup: true, defaultValue: "/tts/tts.php")
    
    
    input("Tones", "boolean", title: "Play Tones?", displayDuringSetup: true, required: true)

}

metadata {
	definition (name: "EspeakWeb", namespace: "shikkie", author: "Michael Moore") {
		capability "Speech Synthesis"
        
        command "TestTTS"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles 
    {		
        standardTile("main", "device.state", canChangeIcon:false) 
        {
            state "default", label:"device.name", canChangeIcon:true, icon:"https://graph.api.smartthings.com/api/devices/icons/st.alarm.beep.beep?displaySize=2x", backgroundColor:"#00FF00"
        }
        
        standardTile("test", "device.Name")
        {
        	state "default", label: "Test TTS", icon: "https://graph.api.smartthings.com/api/devices/icons/st.alarm.beep.beep", action: "TestTTS"
        }
        
        
        
        main(["main"])
        details (["test"] )
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
    
    
    
    

}

def TestTTS()
{
	log.debug "TestTTS"
	return speak("TTS Test Successful")
}


// SpeechSynthesis.speak
def speak(text) {
    log.debug ("speak(${text})")

        
        def host = Ip 
   
        
        def headers = [:] 
        headers.put("HOST", "$host:$Port")
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
        
       // log.debug headers
        
        try
        {
        	def hubAction = new physicalgraph.device.HubAction(
            method: "POST",
            path: Path,
            headers: headers,
            body: [tones: (Tones == "true" ? 1 : 0), text: "$text"]
			)
            
        
        log.debug hubAction
        hubAction
        }
        catch (Exception e) {
            log.debug "Hit Exception $e on $hubAction"
        }

            
      	
}



//////

private String convertIPtoHex(ipAddress) { 
    String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
    log.debug "IP address entered is $ipAddress and the converted hex code is $hex"
    return hex

}

private String convertPortToHex(port) {
	String hexport = port.toString().format( '%04x', port.toInteger() )
   // log.debug hexport
    return hexport
}

private Integer convertHexToInt(hex) {
	Integer.parseInt(hex,16)
}


private String convertHexToIP(hex) {
	log.debug("Convert hex to ip: $hex") 
	[convertHexToInt(hex[0..1]),convertHexToInt(hex[2..3]),convertHexToInt(hex[4..5]),convertHexToInt(hex[6..7])].join(".")
}
/*
private getHostAddress() {
	def parts = device.deviceNetworkId.split(":")
    log.debug device.deviceNetworkId
	def ip = convertHexToIP(parts[0])
	def port = convertHexToInt(parts[1])
	return ip + ":" + port
}
*/
