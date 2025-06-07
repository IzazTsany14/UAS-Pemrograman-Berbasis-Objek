// Initialize when the document is ready
document.addEventListener('DOMContentLoaded', function() {
    // Configure GSAP animations for professional effects
    const configureAnimations = () => {
        // Hero section animation
        gsap.from('.hero-content h1', {
            duration: 1,
            y: 50,
            opacity: 0,
            ease: 'power3.out',
            delay: 0.3
        });
        
        gsap.from('.hero-content p', {
            duration: 0.8,
            y: 30,
            opacity: 0,
            ease: 'power2.out',
            delay: 0.6
        });
        
        gsap.from('.hero-content .btn', {
            duration: 0.8,
            y: 30,
            opacity: 0,
            ease: 'power2.out',
            delay: 0.9,
            stagger: 0.1
        });
        
        gsap.from('.hero-image', {
            duration: 1.2,
            x: 100,
            opacity: 0,
            ease: 'power3.out',
            delay: 0.6
        });
        
        // Section title animations
        gsap.from('.section-title', {
            scrollTrigger: {
                trigger: '.section-title',
                start: 'top 80%'
            },
            duration: 0.8,
            y: 30,
            opacity: 0,
            ease: 'power2.out'
        });
        
        // Category card animations
        gsap.from('.category-card', {
            scrollTrigger: {
                trigger: '.featured-categories',
                start: 'top 70%'
            },
            duration: 0.8,
            y: 50,
            opacity: 0,
            ease: 'power2.out',
            stagger: 0.15
        });
        
        // About section animation
        gsap.from('.about-preview img', {
            scrollTrigger: {
                trigger: '.about-preview',
                start: 'top 70%'
            },
            duration: 1,
            x: -100,
            opacity: 0,
            ease: 'power3.out'
        });
        
        gsap.from('.about-preview .lead', {
            scrollTrigger: {
                trigger: '.about-preview',
                start: 'top 70%'
            },
            duration: 0.8,
            y: 30,
            opacity: 0,
            ease: 'power2.out',
            delay: 0.3
        });
        
        // Testimonial animations
        gsap.from('.testimonial-card', {
            scrollTrigger: {
                trigger: '.testimonials',
                start: 'top 70%'
            },
            duration: 0.8,
            y: 50,
            opacity: 0,
            ease: 'power2.out',
            stagger: 0.15
        });
    };
    
    // Enhanced smooth scroll for anchor links
    const configureSmoothScroll = () => {
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function(e) {
                e.preventDefault();
                
                const targetId = this.getAttribute('href');
                
                if (targetId !== '#') {
                    const targetElement = document.querySelector(targetId);
                    
                    if (targetElement) {
                        const headerHeight = document.querySelector('header')?.offsetHeight || 70;
                        const targetPosition = targetElement.getBoundingClientRect().top + window.pageYOffset - headerHeight;
                        
                        window.scroll({
                            top: targetPosition,
                            behavior: 'smooth'
                        });
                        
                        // Update URL without jumping
                        if (history.pushState) {
                            history.pushState(null, null, targetId);
                        }
                    }
                }
            });
        });
    };
    
    // Initialize all components
    configureAnimations();
    configureSmoothScroll();
});
