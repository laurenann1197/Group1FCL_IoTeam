/* 
 * This class will be used to break down the pcap files obtained from wireshark 
 * into formats readable by our machine learning algorithms.
 */

import net.sf.*;
import org.jnetpcap.*;
import org.jnetpcap.packet.*;
import org.jnetpcap.util.PcapPacketArrayList;

@SuppressWarnings("unused")
public class PCapHandler {
	String FileAddress = "";
	
	public PCapHandler(String FileAddress) {
		this.FileAddress = FileAddress;
	}
	
	/**
	 * Opens the offline Pcap-formatted file. 
	 * @return PcapPacketArrayList  List of packets in the file
	 * @throws ExceptionReadingPcapFiles Facing any error in opening the file
	 */
	
	public PcapPacketArrayList readOfflineFiles() throws Exception {
		//First, setup error buffer and name for our file
		final StringBuilder errbuf = new StringBuilder(); // For any error msgs  
		
		//Second ,open up the selected file using openOffline call
		Pcap pcap = Pcap.openOffline(FileAddress, errbuf);
		
		//Throw exception if it cannot open the file
		if (pcap == null) {  
			throw new Exception(errbuf.toString()); 
		}
		
		//Next, we create a packet handler which will receive packets from the libpcap loop.
		PcapPacketHandler<PcapPacketArrayList> jpacketHandler = new PcapPacketHandler<PcapPacketArrayList>() {  
			public void nextPacket(PcapPacket packet, PcapPacketArrayList PaketsList) {  
				PaketsList.add(packet);
			}  
		};
		
		/*************************************************************************** 
		 * Fourth we enter the loop and tell it to capture unlimited packets.  
		 **************************************************************************/ 
		
		try {  
			PcapPacketArrayList packets = new PcapPacketArrayList();
			pcap.loop(-1,jpacketHandler,packets);
			return packets;
		} finally {  
		//close the pcap handle 
			pcap.close();  
		} 
	}	
}
