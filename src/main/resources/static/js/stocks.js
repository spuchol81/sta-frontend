function createStockChart(canvasId, chartTitle, chartData) {
    const chart = Chart.getChart(canvasId)
    if (chart != null) {
        chart.data.datasets[0].data = chartData.map(row => row.price)
        chart.options.animation = false;
        chart.update()
        return chart
    }
    return new Chart(document.getElementById(canvasId), {
        type: 'line',
        options: {
            scales: {
                x: {
                    display: false,
                },
                y: {
                    display: true,
                }
            },
            plugins: {
                title: {
                    display: true,
                    text: chartTitle,
                },
                legend: {
                    display: false,
                },
                tooltip: {
                    enabled: false,
                },
            }
        },
        data: {
            labels: chartData.map(row => row.time),
            datasets: [{
                data: chartData.map(row => row.price),
                tension: 0.1,
                pointRadius: 0,
            }]
        }
    })
}
