fetch("placement-data.json")
    .then(res => res.json())
    .then(data => {

        const years = data.years;
        const total = data.total_students;
        const placed = data.placed_students;
        const internships = data.internship_students;

        // Placement Bar Chart
        new Chart(document.getElementById("placementChart"), {
            type: "bar",
            data: {
                labels: years,
                datasets: [{
                    label: "Placed Students",
                    data: placed,
                    backgroundColor: "#3b82f6"
                }]
            }
        });

        // Internship Bar Chart
        new Chart(document.getElementById("internshipChart"), {
            type: "bar",
            data: {
                labels: years,
                datasets: [{
                    label: "Internship Students",
                    data: internships,
                    backgroundColor: "#10b981"
                }]
            }
        });

        // Combined line graph
        new Chart(document.getElementById("combinedChart"), {
            type: "line",
            data: {
                labels: years,
                datasets: [
                    {
                        label: "Total Students",
                        data: total,
                        borderColor: "#6b7280",
                        borderWidth: 3
                    },
                    {
                        label: "Placed Students",
                        data: placed,
                        borderColor: "#3b82f6",
                        borderWidth: 3
                    },
                    {
                        label: "Internships",
                        data: internships,
                        borderColor: "#10b981",
                        borderWidth: 3
                    }
                ]
            }
        });

    });