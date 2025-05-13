// Initialize when the document is ready
document.addEventListener('DOMContentLoaded', function() {
    
    // Add animation classes to elements as they appear in viewport
    const animateOnScroll = function() {
        const elements = document.querySelectorAll('.card, .section-title, .lead');
        
        elements.forEach(element => {
            const elementPosition = element.getBoundingClientRect().top;
            const screenPosition = window.innerHeight / 1.2;
            
            if (elementPosition < screenPosition) {
                element.classList.add('animate__animated', 'animate__fadeIn');
            }
        });
    };
    
    // Trigger animation on scroll
    window.addEventListener('scroll', animateOnScroll);
    
    // Initial trigger
    animateOnScroll();
    
    // Smooth scroll for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            
            const targetId = this.getAttribute('href');
            
            if (targetId !== '#') {
                const targetElement = document.querySelector(targetId);
                
                if (targetElement) {
                    window.scrollTo({
                        top: targetElement.offsetTop - 70,
                        behavior: 'smooth'
                    });
                }
            }
        });
    });
    
    // Handle menu item quantity changes
    const quantityInputs = document.querySelectorAll('.quantity-input input');
    
    if (quantityInputs) {
        quantityInputs.forEach(input => {
            input.addEventListener('change', function() {
                if (this.value < 1) {
                    this.value = 1;
                }
            });
        });
    }
    
    // Form validation
    const orderForms = document.querySelectorAll('form[id="checkoutForm"]');
    
    if (orderForms.length > 0) {
        orderForms[0].addEventListener('submit', function(event) {
            let isValid = true;
            
            // Check if payment method is selected
            const paymentMethods = document.querySelectorAll('input[name="paymentMethod"]');
            let paymentSelected = false;
            
            paymentMethods.forEach(method => {
                if (method.checked) {
                    paymentSelected = true;
                }
            });
            
            if (!paymentSelected) {
                alert('Silakan pilih metode pembayaran.');
                isValid = false;
            }
            
            // Validate cash amount if cash payment is selected
            const cashMethod = document.getElementById('cash');
            const amountPaid = document.getElementById('amountPaid');
            
            if (cashMethod && cashMethod.checked && amountPaid) {
                if (parseFloat(amountPaid.value) < parseFloat(amountPaid.min)) {
                    alert('Jumlah uang yang dimasukkan kurang dari total pembayaran.');
                    isValid = false;
                }
            }
            
            if (!isValid) {
                event.preventDefault();
            }
        });
    }
});