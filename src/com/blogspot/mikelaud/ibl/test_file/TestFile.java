package com.blogspot.mikelaud.ibl.test_file;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

import com.blogspot.mikelaud.ibl.Config;
import com.blogspot.mikelaud.ibl.Logger;

// add truncate & exact file resize
public class TestFile {

	private final String MESSAGE_OK = "OK.";
	private final String MESSAGE_FAIL = "FAIL.";
	private final String MESSAGE_PROGRESS = "...";
	//
	private final String FILE_NAME = "2014_01." + Config.getFileExtension();
	private final String FILE_DIRECTORY = Config.getFilesDirectory();
	private final long FILE_TARGET_SIZE_B = Day.getBufferSizeB();
	private final String FILE_RESIZE_MODE = "rw";
	//
	private final EnumSet<StandardOpenOption> CHANNEL_OPEN_OPTIONS = EnumSet.of
	(	StandardOpenOption.READ
	,	StandardOpenOption.WRITE
	);
	
	private void createFile(Path aPath) throws IOException {
		if (Files.notExists(aPath)) {
			final String message = "Create file: %s (0 bytes) %s";
			// create:
			Logger.println(String.format(message, aPath, MESSAGE_PROGRESS));
			Path dirPath = aPath.getParent();
			if (null != dirPath) {
				Files.createDirectories(dirPath);
			}
			Files.createFile(aPath);
			// check:
			if (Files.exists(aPath)) {
				Logger.println(String.format(message, aPath, MESSAGE_OK));
			}
			else {
				Logger.logError(String.format(message, aPath, MESSAGE_FAIL));
			}
		}
	}
	
	private void resizeFile(Path aPath) throws IOException {
		final long fileSizeB = Files.size(aPath);
		if (fileSizeB < FILE_TARGET_SIZE_B) {
			final String message = "Resize file: %s (%d => %d bytes) %s";
			// resize:
			Logger.println(String.format(message, aPath, fileSizeB, FILE_TARGET_SIZE_B, MESSAGE_PROGRESS));
			RandomAccessFile file = new RandomAccessFile(aPath.toFile(), FILE_RESIZE_MODE);
			file.setLength(FILE_TARGET_SIZE_B);
			file.close();
			// check:
			long newFileSizeB = Files.size(aPath);
			if (newFileSizeB >= FILE_TARGET_SIZE_B) {
				Logger.println(String.format(message, aPath, fileSizeB, FILE_TARGET_SIZE_B, MESSAGE_OK));
			}
			else {
				Logger.logError(String.format(message, aPath, fileSizeB, FILE_TARGET_SIZE_B, MESSAGE_FAIL));
				Logger.logError(String.format("Current file size: %d bytes (%s).", newFileSizeB, aPath));
			}
		}
	}
	
	private void mapFile(Path aPath) throws IOException {
		final String message = "Map file: %s %s";
		// map:
		Logger.println(String.format(message, aPath, MESSAGE_PROGRESS));
		FileChannel fileChannel = FileChannel.open(aPath, CHANNEL_OPEN_OPTIONS);
		MappedByteBuffer buffer = fileChannel.map(MapMode.READ_WRITE, 0, FILE_TARGET_SIZE_B);
		buffer.force();
		fileChannel.close();
		Logger.println(String.format(message, aPath, MESSAGE_OK));
	}
	
	public void run() throws IOException {
		//
		Path path = Paths.get(FILE_DIRECTORY, FILE_NAME);
		createFile(path);
		resizeFile(path);
		mapFile(path);
		//
		Logger.println("Done.");		
	}

}
