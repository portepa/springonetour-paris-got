package io.pivotal.demos1tparis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class VoteController {

    VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    private final VoteRepository voteRepository;

    @GetMapping(value = "vote")
    public Iterable<Vote> getVotes() {
        return voteRepository.findAll();
    }

    @PostMapping(value="vote")
    public Vote createVote(@RequestBody Vote vote) {
        System.out.println("HELLO");
        System.out.println(vote);
        return voteRepository.save(vote);
    }
}