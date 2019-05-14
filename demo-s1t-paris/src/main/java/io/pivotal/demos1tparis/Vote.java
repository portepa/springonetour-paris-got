package io.pivotal.demos1tparis;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Vote {
    protected Vote() {}
    protected  Vote(int voteIndex) {
        this.voteIndex = voteIndex;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int voteIndex;
}
