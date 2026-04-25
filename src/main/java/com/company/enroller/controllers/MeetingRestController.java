package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    private ResponseEntity<?> participantAlreadyExists(String login) {
        return new ResponseEntity<>("Unable to create. A participant with login " + login + " already exist.",
                HttpStatus.CONFLICT);
    }

    private ResponseEntity<?> participantDoesntExists(String login) {
        String.format("Unable to create. A participant with login %s doesn't exist.", login);
        return new ResponseEntity<>("Unable to create. A participant with login " + login + " doesn't exist.",
                HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        return new ResponseEntity<>(meetingService.getAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable String id) {
        return new ResponseEntity<>(meetingService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
    public ResponseEntity<?> getMeetingsParticipants(@PathVariable String id) {
        Meeting foundMeeting = meetingService.findById(id);
        if (foundMeeting == null) {
            return participantDoesntExists("");
        }
        return new ResponseEntity<>(meetingService.addMeeting(foundMeeting), HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
//        if(meetingService.exists(meeting)){
        if (meetingService.findById(meeting.getId())!=null) {
            return participantAlreadyExists("");
        }
        return new ResponseEntity<>(meetingService.addMeeting(meeting), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateParticipant(@PathVariable String id,
                                               @RequestBody Meeting meeting) {
        if (meetingService.findById(id)==null) {
            return participantDoesntExists("login");
        }
        meetingService.updateMeeting(id, meeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@PathVariable String id) {
        Meeting foundMeeting = meetingService.findById(id);
        if (foundMeeting == null) {
            return participantDoesntExists("login");
        }
        meetingService.deleteMeeting(foundMeeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
