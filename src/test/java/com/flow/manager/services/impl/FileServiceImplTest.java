package com.flow.manager.services.impl;

import com.flow.manager.service.FileService;
import com.flow.manager.service.ServicesHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceImplTest {

    @Autowired
    ServicesHandler servicesHandler;

    @Autowired
    FileService driveFileService;

    @Before
    public void onSetup(){
        try{
            servicesHandler.initServices("hardcorewithin");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void testGetPlaylistTitle(){

        String playlistTitle = null;
        try {

            playlistTitle = driveFileService.retrievePlaylistTitleFromDriveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("Flow #TEST",playlistTitle);
    }

    @Test
    public void testLoadPlaylistItems(){

        try {
            List<String> playlistItems = driveFileService.loadPlaylistItemsFromDriveFile();
            assertEquals(2,playlistItems.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
