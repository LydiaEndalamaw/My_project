<script>
    // Example: Increase cart count when Add to Cart is clicked
    const buttons = document.querySelectorAll('.btn');
    const cartCount = document.getElementById('cart-count');

    let count = 0;

    buttons.forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault(); // prevent default link behavior
            count++;
            cartCount.textContent = count;
            alert('Item added to cart!');
        });
    });
</script>
