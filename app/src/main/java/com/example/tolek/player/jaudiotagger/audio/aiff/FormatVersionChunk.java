package com.example.tolek.player.jaudiotagger.audio.aiff;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

import com.example.tolek.player.jaudiotagger.audio.generic.Utils;

public class FormatVersionChunk extends Chunk {
    
    private AiffAudioHeader aiffHeader;
    
    /**
     * Constructor.
     * 
     * @param hdr      The header for this chunk
     * @param raf      The file from which the AIFF data are being read
     * @param aHdr     The AiffTag into which information is stored
     */
    public FormatVersionChunk (
            ChunkHeader hdr, 
            RandomAccessFile raf,
            AiffAudioHeader aHdr)
    {
        super (raf, hdr);
        aiffHeader = aHdr;
    }
    
    /** Reads a chunk and extracts information. 
     * 
     *  @return   <code>false</code> if the chunk is structurally
     *            invalid, otherwise <code>true</code>
     */
    public boolean readChunk () throws IOException
    {
        long rawTimestamp = Utils.readUint32(raf);
        // The timestamp is in seconds since January 1, 1904.
        // We must convert to Java time.
        Date timestamp = AiffUtil.timestampToDate (rawTimestamp);
        aiffHeader.setTimestamp(timestamp);
        return true;
    }

}
