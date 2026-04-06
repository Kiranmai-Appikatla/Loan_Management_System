package com.safehaven.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.safehaven.entity.CounsellingNote;
import com.safehaven.entity.LegalAdvice;
import com.safehaven.entity.ResourceItem;
import com.safehaven.entity.Role;
import com.safehaven.entity.SupportRequest;
import com.safehaven.entity.SupportStatus;
import com.safehaven.entity.User;
import com.safehaven.repository.CounsellingNoteRepository;
import com.safehaven.repository.LegalAdviceRepository;
import com.safehaven.repository.ResourceItemRepository;
import com.safehaven.repository.SupportRequestRepository;
import com.safehaven.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ResourceItemRepository resourceItemRepository;
    private final SupportRequestRepository supportRequestRepository;
    private final LegalAdviceRepository legalAdviceRepository;
    private final CounsellingNoteRepository counsellingNoteRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            UserRepository userRepository,
            ResourceItemRepository resourceItemRepository,
            SupportRequestRepository supportRequestRepository,
            LegalAdviceRepository legalAdviceRepository,
            CounsellingNoteRepository counsellingNoteRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.resourceItemRepository = resourceItemRepository;
        this.supportRequestRepository = supportRequestRepository;
        this.legalAdviceRepository = legalAdviceRepository;
        this.counsellingNoteRepository = counsellingNoteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        User admin = userRepository.save(new User("Aisha Admin", "admin@safehaven.org", passwordEncoder.encode("SafeHaven123"), Role.ADMIN));
        User survivor = userRepository.save(new User("Maya Survivor", "survivor@safehaven.org", passwordEncoder.encode("SafeHaven123"), Role.VICTIM_SURVIVOR));
        User counsellor = userRepository.save(new User("Nina Counsellor", "counsellor@safehaven.org", passwordEncoder.encode("SafeHaven123"), Role.COUNSELLOR));
        User advisor = userRepository.save(new User("Rahul Legal", "legal@safehaven.org", passwordEncoder.encode("SafeHaven123"), Role.LEGAL_ADVISOR));

        resourceItemRepository.save(new ResourceItem(
                "Emergency Safety Planning",
                "Steps to prepare an emergency exit plan, gather essential documents, and identify immediate safe contacts.",
                "SAFETY",
                "https://www.thehotline.org/"
        ));
        resourceItemRepository.save(new ResourceItem(
                "Legal Rights After Abuse",
                "Overview of protection orders, evidence documentation, child custody considerations, and survivor-focused legal options.",
                "LEGAL_RIGHTS",
                "https://www.womenslaw.org/"
        ));
        resourceItemRepository.save(new ResourceItem(
                "Health Risks and Trauma Response",
                "Information on physical warning signs, emotional trauma, and how to seek confidential medical attention quickly.",
                "HEALTH_RISKS",
                "https://www.who.int/"
        ));

        SupportRequest supportRequest = supportRequestRepository.save(new SupportRequest(
                survivor,
                counsellor,
                advisor,
                "I need help understanding my immediate safety options and whether I can file for legal protection this week.",
                SupportStatus.IN_PROGRESS,
                "Counselling intake completed and legal review started"
        ));

        counsellingNoteRepository.save(new CounsellingNote(
                supportRequest,
                "Established a short-term safety plan and shared breathing exercises for panic response.",
                counsellor.getName()
        ));
        legalAdviceRepository.save(new LegalAdvice(
                supportRequest,
                "Document incidents with dates, keep copies off-device if possible, and prepare for a protection order consultation.",
                advisor.getName()
        ));
    }
}
