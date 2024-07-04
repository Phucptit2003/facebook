package org.example.controllers;

import org.example.objects.CommentDTO;
import org.example.objects.InteractionsDTO;
import org.example.services.InteractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interactions")
public class InteractionController {
    private final InteractService interactionService;

    @Autowired
    public InteractionController(InteractService interactionService) {
        this.interactionService = interactionService;
    }

    @PostMapping("/like")
    public ResponseEntity<Void> likePost(@RequestBody InteractionsDTO interactionsDTO) {
        interactionService.toggleLike(interactionsDTO.getUsername(), interactionsDTO.getPostId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/comment")
    public ResponseEntity<Void> commentPost(@RequestBody CommentDTO interactionsDTO) {
        interactionService.commentPost(interactionsDTO.getUsername(), interactionsDTO.getPostId(), interactionsDTO.getContent());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/share")
    public ResponseEntity<Void> sharePost(@RequestBody InteractionsDTO interactionsDTO) {
        interactionService.sharePost(interactionsDTO.getUsername(), interactionsDTO.getPostId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

