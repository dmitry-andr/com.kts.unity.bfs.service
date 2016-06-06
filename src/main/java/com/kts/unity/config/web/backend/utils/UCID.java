package com.kts.unity.config.web.backend.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

public class UCID implements Serializable {
	
	private static final long serialVersionUID = 2686207060455626462L;
	
	private static long  DELAY_TIME = 500; // in milliseconds
	
	private static final String ZERO_STRING = "0";
	private static final int hostrnd = getHostBasedNum();
	private static final Object lock = new Object();
	
	private static long lastTime = System.currentTimeMillis();
	private static short MAX_COUNT = 999;	// from 0 till 999
	private static short lastCount = 0;	// from 0 till 999

	/**
	 * base number that uniquely identifies the VM that this <code>UID</code>
	 * was generated in with respect to its host and at the given time
	 * Part of this number used in UID (some random)
	 * @serial
	 */
	private final int rndpart;

	/**
	 * a time (as returned by {@link System#currentTimeMillis()}) at which
	 * the VM that this <code>UID</code> was generated in was alive
	 * Part of this time with period near 3 years are used in UID
	 * @serial
	 */
	private final long time;

	/**
	 * 3 digit number to distinguish <code>UID</code> instances created
	 * in the same VM with the same time value
	 * @serial
	 */
	private final short count;

	/**
	 * Constructs a <code>UID</code> given data read from a stream.
	 */
	private UCID(int unique, long time, short count) {
		this.rndpart = unique;
		this.time = time;
		this.count = count;
	}

	public UCID() {
		this.rndpart = hostrnd;

		synchronized (lock)
		{
			if (lastCount == MAX_COUNT)
			{
				boolean done = false;
				
				while (!done)
				{
					long now = System.currentTimeMillis();
					
					if (now < (lastTime + DELAY_TIME)) {
						// pause for a second to wait for time to change
						try {
							Thread.sleep(DELAY_TIME);
						} 
						catch (java.lang.InterruptedException e) {
							// ignore exception
						}	
						
						continue;
					}
					
					lastTime = now;
					lastCount = 0;
					done = true;
				}
			}
			
			this.time = lastTime;
			this.count = lastCount++;
		}
	}

	/**
	 * In the absence of an actual pid, just do something somewhat
	 * random.
	 * must be number with not more then 19-12-3=4 digits
	 */
	private static int getHostBasedNum() {
		int iID = (new Object()).hashCode();
		
		while (iID > 9223)
		{
			iID = iID - ((iID >> 13) << 13);	// rest of 8192
		}
		
		return iID;
	}

	public Long toLong() {
		
		StringBuffer buf = new StringBuffer(20);	// max for long
		String stime = Long.toString(this.time);
		
		buf.append(Integer.toString(this.rndpart));
		
		if (stime.length() > 12) {
			buf.append(stime.substring(stime.length()-12));
		}
		else {
			buf.append(stime);
		}
		
		if (this.count < 100) {
			buf.append(ZERO_STRING);
		}
		
		if (this.count < 10) {
			buf.append(ZERO_STRING);
		}
		
		buf.append(Integer.toString(this.count));
		return new Long(buf.toString());
	}

	/**
	 * like java.rmi.server.UID().toString();
	 */
	public String toString() {
		return Integer.toString(this.rndpart,16) + ":" + Long.toString(this.time,16) + ":" + Integer.toString(this.count,16);
	}

	/**
	 * Returns the hash code value for this <code>UID</code>.
	 *
	 * @return	the hash code value for this <code>UID</code>
	 */
	public int hashCode() {
		return (int) this.rndpart + (int) this.time + (int) this.count;
	}

	/**
	 * Compares the specified object with this <code>UCID</code> for
	 * equality.
	 *
	 * This method returns <code>true</code> if and only if the
	 * specified object is a <code>UCID</code> instance with the same
	 * <code>rndpart</code>, <code>time</code>, and <code>count</code>
	 * values as this one.
	 *
	 * @param	obj the object to compare this <code>UID</code> to
	 *
	 * @return	<code>true</code> if the given object is equivalent to
	 * this one, and <code>false</code> otherwise
	 */
	public boolean equals(Object obj) {
		boolean bres = false;
		
		if ((obj != null) && (obj instanceof UCID))
		{
			UCID ucid = (UCID)obj;
			
			bres = (this.rndpart == ucid.rndpart && this.count == ucid.count && this.time == ucid.time);
		}
		
		return bres;
	}

	/**
	 * Marshals a binary representation of this <code>UCID</code> to
	 * a <code>DataOutput</code> instance.
	 *
	 * @param	out the <code>DataOutput</code> instance to write
	 * this <code>UCID</code> to
	 *
	 * @throws	IOException if an I/O error occurs while performing
	 * this operation
	 */
	public void write(DataOutput out) throws IOException {
		out.writeInt(this.rndpart);
		out.writeLong(this.time);
		out.writeShort(this.count);
	}

	/**
	 * Constructs and returns a new <code>UCID</code> instance by
	 * unmarshalling a binary representation from an
	 * <code>DataInput</code> instance.
	 *
	 * @param	in the <code>DataInput</code> instance to read
	 * <code>UCID</code> from
	 *
	 * @return	unmarshalled <code>UCID</code> instance
	 *
	 * @throws	IOException if an I/O error occurs while performing
	 * this operation
	 */
	public static UCID read(DataInput in) throws IOException {
		int uniquepart = in.readInt();
		long time = in.readLong();
		short count = in.readShort();
		return new UCID(uniquepart, time, count);
	}

}
