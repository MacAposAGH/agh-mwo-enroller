package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ErrorHandler;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    ParticipantService participantService;

    @Autowired
    MeetingService meetingService;


    @Autowired
    ErrorHandler errorHandler;

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

        }
        return new ResponseEntity<>(meetingService.addMeeting(foundMeeting), HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
//        if(meetingService.exists(meeting)){
        if (meetingService.findById(meeting.getId()) != null) {
            return errorHandler.entityAlreadyExist();
        }
        return new ResponseEntity<>(meetingService.addMeeting(meeting), HttpStatus.CREATED);
    }
//
//    @RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
//    public ResponseEntity<?> registerMeetingsParticipant(@PathVariable String id, @RequestBody Participant participant) {
//        Meeting foundMeeting = meetingService.findById(id);
//        if (foundMeeting == null) {
//            return errorHandler.entityDoesntExist();
//        }
//        Participant foundParticipant = participantService.findByLogin(participant.getLogin());
//        if (foundParticipant == null) {
//            return errorHandler.entityDoesntExist();
//        }
//        foundMeeting.getParticipants().add(foundParticipant);
//        meetingService.updateMeeting(id, foundMeeting);
//        return new ResponseEntity<>(meetingService.addMeeting(foundMeeting), HttpStatus.CREATED);
//    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateParticipant(@PathVariable String id,
                                               @RequestBody Meeting meeting) {
        if (meetingService.findById(id) == null) {
            return errorHandler.entityDoesntExist();
        }
        meetingService.updateMeeting(id, meeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@PathVariable String id) {
        Meeting foundMeeting = meetingService.findById(id);
        if (foundMeeting == null) {
            return errorHandler.entityDoesntExist();
        }
        meetingService.deleteMeeting(foundMeeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
