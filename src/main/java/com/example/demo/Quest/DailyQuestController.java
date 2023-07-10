package com.example.demo.Quest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/daily-quests")
public class DailyQuestController {
    @Autowired
    private DailyQuestRepository dailyQuestRepository;

    @GetMapping
    public List<DailyQuest> getDailyQuests() {
        return dailyQuestRepository.findAll();
    }

    @PostMapping
    public DailyQuest createDailyQuest(@RequestBody DailyQuest dailyQuest) {
        return dailyQuestRepository.save(dailyQuest);
    }

    @PutMapping("/{id}/complete")
    public DailyQuest completeDailyQuest(@PathVariable Long id) {
        DailyQuest dailyQuest = dailyQuestRepository.findById(id).orElse(null);
        if (dailyQuest != null) {
            dailyQuest.setCompleted(true);
            return dailyQuestRepository.save(dailyQuest);
        }
        return null;
    }
}

