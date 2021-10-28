package com.epam.parsers;

import com.epam.exceptions.FileParseException;
import org.farng.mp3.TagException;

import java.io.IOException;
import java.io.InputStream;

public interface AudioParser {

    Mp3Metadata getMetadata(InputStream stream)  throws TagException, FileParseException, IOException;

}
